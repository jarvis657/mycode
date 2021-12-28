package newfsmengine

type EventProcessor interface {
	OnExit(leaveAction string, fromState int32, toState int32, args []interface{}) (bool, error)
	Action(action string, fromState int32, toState int32, args []interface{}) (bool, error)
	OnActionFailure(action string, fromState int32, toState int32, args []interface{}, err error) (bool, error)
	OnEnter(enterAction string, fromState int32, toState int32, args []interface{}) (bool, error)
}

type DefaultDelegate struct {
	P               EventProcessor
}

func (dd *DefaultDelegate) HandleEvent(transition *Transition, args []interface{}) error {
	var canPass bool
	var err error
	if transition.From != transition.To {
		canPass, err = dd.P.OnExit(transition.ExitAction, transition.From, transition.To, args)
	}
	if !canPass {
		return nil
	}

	canPass, err = dd.P.Action(transition.Action, transition.From, transition.To, args)
	if err != nil {
		dd.P.OnActionFailure(transition.Action, transition.From, transition.To, args, err)
	}
	if !canPass {
		return nil
	}
	if transition.From != transition.To {
		dd.P.OnEnter(transition.EnterAction, transition.From, transition.To, args)
	}
	return nil
}
//func (m *DefaultDelegate) GetNextTrigger() []interface{} {
//	return m.nextTriggerArgs
//}
//func (m *DefaultDelegate) NextTrigger(currentState int32, event string, args ...interface{}) {
//	if len(m.nextTriggerArgs) >= 4 {
//		m.nextTriggerArgs = nil
//	}
//	m.nextTriggerArgs = append(m.nextTriggerArgs, currentState, event)
//	m.nextTriggerArgs = append(m.nextTriggerArgs, args...)
//	//for _, arg := range args {
//	//	m.nextTriggerArgs = append(m.nextTriggerArgs, arg)
//	//}
//}
