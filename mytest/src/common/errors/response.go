package errors

import (
	"mytest.com/src/common/constant"
	"git.code.oa.com/trpc-go/trpc-go/errs"
)

func Response(err *error) (code int32, msg string) {
	if *err != nil {
		code = int32(errs.Code(*err))
		msg = errs.Msg(*err)
	} else {
		code = 0
		msg = constant.NormalSuccess
	}
	return
}
