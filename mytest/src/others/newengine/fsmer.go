package newfsmengine

import (
	. "mytest.com/src/others/newengine/fsmengine"
	"reflect"
)

var StartEvent = "start"
var ContinueEvent = "continue"
var PauseEvent = "pause"
var RollbackEvent = "rollback"
var AutoSuccessEvent = "autoSuccess"
var AutoFailEvent = "autoFail"
var NoneEvent = "none"

func transEvent(isDone bool) string {
	if isDone {
		return AutoSuccessEvent
	} else {
		return AutoFailEvent
	}
}
func transWebEvent(event int32) string {
	switch event {
	case constant.ActionStart:
		return StartEvent
	case constant.ActionContinuer:
		return ContinueEvent
	case constant.ActionPause:
		return PauseEvent
	case constant.ActionRollBack:
		return RollbackEvent
	}
	return ""
}

//执行顺序 (离开当前状态动作ExitAction)->进入当前状态动作Action->上步失败处理OnActionFailure->进入目标状态动作EnterAction
func initFSM(processor *releaseEngineServiceImpl) *StateMachine {
	delegate := &DefaultDelegate{P: processor}
	transitions := []Transition{
		Transition{From: constant.DeployShould, Event: AutoSuccessEvent, To: constant.Deploying, Action: "DeployShouldToTaskDeploying", ExitAction: "", EnterAction: "EnterStateAction"},
		Transition{From: constant.Deploying, Event: PauseEvent, To: constant.DeployPaused, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
		Transition{From: constant.DeployPaused, Event: ContinueEvent, To: constant.Deploying, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
		Transition{From: constant.Deploying, Event: AutoFailEvent, To: constant.DeployFailed, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
		Transition{From: constant.Deploying, Event: AutoSuccessEvent, To: constant.DeploySuccess, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},

		//TODO机器处理
		//Transition{From: constant.DeployFailed, Event: RollbackEvent, To: constant.RollbackShould, Action: "", ExitAction: "", EnterAction: ""},
		//Transition{From: constant.DeploySuccess, Event: RollbackEvent, To: constant.RollbackShould, Action: "", ExitAction: "", EnterAction: ""},

		Transition{From: constant.RollbackShould, Event: PauseEvent, To: constant.RollbackPaused, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
		Transition{From: constant.RollbackPaused, Event: PauseEvent, To: constant.RollbackShould, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
		Transition{From: constant.RollbackShould, Event: AutoSuccessEvent, To: constant.Rollbacking, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
		Transition{From: constant.Rollbacking, Event: AutoFailEvent, To: constant.RollbackFailed, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
		Transition{From: constant.Rollbacking, Event: AutoSuccessEvent, To: constant.RollbackSuccess, Action: "", ExitAction: "", EnterAction: "EnterStateAction"},
	}
	return NewStateMachine(delegate, transitions...)
}

func (s *releaseEngineServiceImpl) Action(action string, fromState int32, toState int32, args []interface{}) (bool, error) {
	canPass := true
	var err error
	if len(action) > 0 {
		args = append(args, fromState)
		args = append(args, toState)
		canPass, err = InvokeObjectMethod(s, action, args...)
	}
	return canPass, err
}

func (s *releaseEngineServiceImpl) OnActionFailure(action string, fromState int32, toState int32, args []interface{}, err error) (bool, error) {
	canPass := true
	if len(action) > 0 {
		args = append(args, fromState)
		args = append(args, toState)
		canPass, err = InvokeObjectMethod(s, action, args...)
	}
	return canPass, err
}

func (s *releaseEngineServiceImpl) OnExit(leaveAction string, fromState int32, toState int32, args []interface{}) (bool, error) {
	canPass := true
	var err error
	if len(leaveAction) > 0 {
		args = append(args, fromState)
		args = append(args, toState)
		canPass, err = InvokeObjectMethod(s, leaveAction, args...)
	}
	return canPass, err
}

func (s *releaseEngineServiceImpl) OnEnter(enterAction string, fromState int32, toState int32, args []interface{}) (bool, error) {
	canPass := true
	var err error
	if len(enterAction) > 0 {
		args = append(args, fromState)
		args = append(args, toState)
		canPass, err = InvokeObjectMethod(s, enterAction, args...)
	}
	return canPass, err
}

//InvokeObjectMethod 调用的方法
func InvokeObjectMethod(object interface{}, methodName string, args ...interface{}) (bool, error) {
	inputs := make([]reflect.Value, len(args))
	for i, _ := range args {
		inputs[i] = reflect.ValueOf(args[i])
	}
	call := reflect.ValueOf(object).MethodByName(methodName).Call(inputs)
	var err error
	if v, ok := call[1].Interface().(error); ok {
		err = v
	}
	return call[0].Bool(), err
}

//TODO 还需要添加判断是否要拉取现在部署状态的方法。
func checkDone(statusCode int32) bool {
	if statusCode == constant.DeployFailed || statusCode == constant.DeploySuccess || statusCode == constant.RollbackFailed || statusCode == constant.RollbackSuccess || statusCode == constant.Pass {
		return true
	}
	return false
}
