package newfsmengine

import (
	"fmt"
)

type Transition struct {
	From        int32
	Event       string
	To          int32
	Action      string
	ExitAction  string
	EnterAction string
}

type Delegate interface {
	HandleEvent(transition *Transition, args []interface{}) error
}

type StateMachine struct {
	delegate    Delegate
	transitions []Transition
	//nextTriggerArgs []interface{}
}

type Error interface {
	error
	BadEvent() string
	CurrentState() string
}

type smError struct {
	badEvent     string
	currentState int32
}

func (e smError) Error() string {
	return fmt.Sprintf("state machine error: cannot find transition for event [%s] when in state [%s]\n", e.badEvent, e.currentState)
}

func (e smError) BadEvent() string {
	return e.badEvent
}

func (e smError) CurrentState() int32 {
	return e.currentState
}

func NewStateMachine(delegate Delegate, transitions ...Transition) *StateMachine {
	return &StateMachine{delegate: delegate, transitions: transitions}
}

func (m *StateMachine) Trigger(currentState int32, event string, args ...interface{}) error {
	tran := m.FindTransMatching(currentState, event)
	if tran == nil {
		return smError{event, currentState}
	}

	var err error
	if tran.Action != "" {
		err = m.delegate.HandleEvent(tran, args)
	}
	//if len(m.nextTriggerArgs) >= 4 {
	//	err = m.Trigger(cast.ToInt32(m.nextTriggerArgs[0]), fmt.Sprint(m.nextTriggerArgs[1]), m.nextTriggerArgs[2:]...)
	//	m.nextTriggerArgs = nil
	//}
	return err
}

//
//func (m *StateMachine) NextTrigger(currentState int32, event string, args ...interface{}) {
//	if len(m.nextTriggerArgs) >= 4 {
//		m.nextTriggerArgs = nil
//	}
//	m.nextTriggerArgs = append(m.nextTriggerArgs, currentState, event)
//	m.nextTriggerArgs = append(m.nextTriggerArgs,args...)
//	//for _, arg := range args {
//	//	m.nextTriggerArgs = append(m.nextTriggerArgs, arg)
//	//}
//}

func (m *StateMachine) FindTransMatching(fromState int32, event string) *Transition {
	for _, v := range m.transitions {
		if v.From == fromState && v.Event == event {
			return &v
		}
	}
	return nil
}
