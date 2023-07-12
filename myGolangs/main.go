package main

import (
	//. "./fsmengine"
	"fmt"
	"reflect"
)

type Turnstile struct {
	ID         uint64
	EventCount uint64   //事件统计
	CoinCount  uint64   //投币事件统计
	PassCount  uint64   //顾客通过事件统计
	State      string   //当前状态
	States     []string //历史经过的状态
}

//var processor = &TurnstileEventProcessor{}
//
//func (p *TurnstileEventProcessor) Test(t *Turnstile, a string, b string) (string, error) {
//	p.Test3(a, b)
//	states := t.States
//	//states := append(states, "aaavv")
//	states[0] = "vvvvvv"
//	for i := range states {
//		fmt.Println(fmt.Sprint(i) + "===============================\n")
//	}
//	return "vvvvvvvv", nil
//}

//	func (p *TurnstileEventProcessor) Test3(a string, b string) (string, error) {
//		fmt.Println("===============itest33333333333================" + a + "---" + b)
//		return "vvvvvvvv", nil
//	}
func main() {
	ts := &Turnstile{
		ID:     1,
		State:  "Locked",
		States: []string{"Locked"},
	}
	//processor.fsm = initFSM()
	//err := processor.fsm.Trigger(ts.State, "Push", ts, "a", "b")
	fmt.Printf(">>>%+v", ts)
	//if err != nil {
	//	fmt.Errorf("trigger err: %v", err)
	//}
	fmt.Println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv")
	var strss = []string{
		"qwr",
		"234",
		"yui",
	}
	fmt.Println("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv")

	var strss2 = []string{
		"qqq",
		"aaa",
		"zzz",
		"zzz",
	}
	strss = append(strss, strss2...) //strss2的元素被打散一个个append进strss
	fmt.Println(strss)
	NextTrigger(32, "a", "b", "c", "d")
}
func NextTrigger(currentState int32, event string, args ...interface{}) {
	var nextTriggerArgs []interface{}
	nextTriggerArgs = append(nextTriggerArgs, currentState, event)
	for i, arg := range args {
		fmt.Println("i" + fmt.Sprint(i) + " arg:" + fmt.Sprint(arg))
	}
	fmt.Println(nextTriggerArgs)
}

//func initFSM() *StateMachine {
//	delegate := &DefaultDelegate{P: processor}
//	transitions := []Transition{
//		Transition{From: "Locked", Event: "Push", To: "Open", Action: "Test"},
//	}
//	return NewStateMachine(delegate, transitions...)
//}
//
//type TurnstileEventProcessor struct {
//	fsm *StateMachine
//}

//	func (p *TurnstileEventProcessor) Action(action string, fromState string, toState string, args []interface{}) error {
//		if action == "Test" {
//			fmt.Printf("%v.........invoke.............%+v\n", action)
//			InvokeObjectMethod(p, action, args...)
//		}
//		return nil
//	}
func InvokeObjectMethod(object interface{}, methodName string, args ...interface{}) {
	inputs := make([]reflect.Value, len(args))
	for i, _ := range args {
		inputs[i] = reflect.ValueOf(args[i])
	}
	reflect.ValueOf(object).MethodByName(methodName).Call(inputs)
}

//func (p *TurnstileEventProcessor) OnActionFailure(action string, fromState string, toState string, args []interface{}, err error) {
//	fmt.Printf("%v.........invoke.............%+v\n", "OnActionFailure", action)
//}
//
//func (p *TurnstileEventProcessor) OnExit(fromState string, args []interface{}) {
//	fmt.Printf("%v.........invoke.............%+v\n", "OnExit", fromState)
//}
//
//func (p *TurnstileEventProcessor) OnEnter(toState string, args []interface{}) {
//	fmt.Printf("%v.........invoke.............%+v\n", "OnEnter", toState)
//}
