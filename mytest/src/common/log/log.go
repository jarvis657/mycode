package log

// 用于日志上报的 key
const (
	RequestId     = "request_id"
	CallerService = "caller_service"
	CalleeService = "callee_service"
	CallerIp      = "caller_ip"
	CalleeIp      = "callee_ip"
	MethodName    = "method_name"
	EnvName       = "env_name"
	UserName      = "user_name"

	Level    = "level"
	FileName = "file_name"
	LineNo   = "line_no"

	LevelTrace = "trace"
	LevelDebug = "debug"
	LevelInfo  = "info"
	LevelWarn  = "warn"
	LevelError = "error"
	LevelFatal = "fatal"

	StaffName = "STAFFNAME"
)

// 用于设置天机阁上报的染色 key
//func SetDyeingKey(ctx context.Context, key string) {
//	tlog.SetDyeingKey(ctx, key)
//}
//
//func WithContextFields(ctx context.Context, fields ...string) {
//	log.WithContextFields(ctx, fields...)
//}
//
//func logWithContextFields(ctx context.Context, level string, args ...interface{}) {
//	setLogger(ctx, level)
//
//	// tjg 的日志输出接口中封装了 trpc log 库，避免日志输出两次，直接调用 tlog 输出
//	if f, ok := logMap[level]; ok {
//		tlog.SetDyeing(ctx)
//		f(ctx, args...)
//	}
//}
//
//func logFmtWithContextFields(ctx context.Context, level string, format string, args ...interface{}) {
//	setLogger(ctx, level)
//
//	// tjg 的日志输出接口中封装了 trpc log 库，避免日志输出两次，直接调用 tlog 输出
//	if f, ok := logFmtMap[level]; ok {
//		tlog.SetDyeing(ctx)
//		f(ctx, format, args...)
//	}
//}
//
//func setLogger(ctx context.Context, level string) {
//	_, fileName, lineNo, _ := runtime.Caller(3)
//	_, fileName = filepath.Split(fileName)
//
//	msg := trpc.Message(ctx)
//
//	userName, ok := ctx.Value(StaffName).(string)
//	if !ok {
//		userName = ""
//	}
//
//	var callerIp, calleeIp string = "", ""
//	if msg.LocalAddr() != nil {
//		callerIp = msg.LocalAddr().String()
//	}
//	if msg.RemoteAddr() != nil {
//		calleeIp = msg.RemoteAddr().String()
//	}
//
//	traceId := GetTraceId(ctx)
//
//	newLogger := log.WithFields(
//		RequestId, traceId,
//		CallerService, msg.CallerServiceName(),
//		CallerIp, callerIp,
//		CalleeService, msg.CalleeServiceName(),
//		CalleeIp, calleeIp,
//		MethodName, msg.ServerRPCName(),
//		EnvName, msg.EnvName(),
//		UserName, userName,
//		Level, level,
//		FileName, fileName,
//		LineNo, strconv.Itoa(lineNo))
//
//	trpc.Message(ctx).WithLogger(newLogger)
//}
//
//func DebugContext(ctx context.Context, args ...interface{}) {
//	logWithContextFields(ctx, LevelDebug, args...)
//}
//
//func DebugContextf(ctx context.Context, format string, args ...interface{}) {
//	logFmtWithContextFields(ctx, LevelDebug, format, args...)
//}
//
//func InfoContext(ctx context.Context, args ...interface{}) {
//	logWithContextFields(ctx, LevelInfo, args...)
//}
//
//func InfoContextf(ctx context.Context, format string, args ...interface{}) {
//	logFmtWithContextFields(ctx, LevelInfo, format, args...)
//}
//
//func WarnContext(ctx context.Context, args ...interface{}) {
//	logWithContextFields(ctx, LevelWarn, args...)
//}
//
//func WarnContextf(ctx context.Context, format string, args ...interface{}) {
//	logFmtWithContextFields(ctx, LevelWarn, format, args...)
//}
//
//func ErrorContext(ctx context.Context, args ...interface{}) {
//	logWithContextFields(ctx, LevelError, args...)
//}
//
//func ErrorContextf(ctx context.Context, format string, args ...interface{}) {
//	logFmtWithContextFields(ctx, LevelError, format, args...)
//}
//
//func FatalContext(ctx context.Context, args ...interface{}) {
//	logWithContextFields(ctx, LevelFatal, args)
//}
//
//func FatalContextf(ctx context.Context, format string, args ...interface{}) {
//	logFmtWithContextFields(ctx, LevelFatal, format, args...)
//}
//
//func Debug(args ...interface{}) {
//	log.Debug(args...)
//}
//
//func Debugf(format string, args ...interface{}) {
//	log.Debugf(format, args...)
//}
//
//func Info(args ...interface{}) {
//	log.Info(args...)
//}
//
//func Infof(format string, args ...interface{}) {
//	log.Infof(format, args...)
//}
//
//func Warn(args ...interface{}) {
//	log.Warn(args...)
//}
//
//func Warnf(format string, args ...interface{}) {
//	log.Warnf(format, args...)
//}
//
//func Error(args ...interface{}) {
//	log.Error(args...)
//}
//
//func Errorf(format string, args ...interface{}) {
//	log.Errorf(format, args...)
//}
//
//func Fatal(args ...interface{}) {
//	log.Fatal(args...)
//}
//
//func Fatalf(format string, args ...interface{}) {
//	log.Fatalf(format, args...)
//}
//
//func GetTraceId(ctx context.Context) string {
//	//span := opentracing.SpanFromContext(ctx)
//	//if span == nil {
//	//	return ""
//	//}
//	//
//	//tjgSpan := span.(*tjg.Span)
//	//
//	//return tjgSpan.GetHexTraceId()
//}
//
//type logFmtFun func(ctx context.Context, format string, args ...interface{})
//
//var logFmtMap map[string]logFmtFun
//
//type logFun func(ctx context.Context, args ...interface{})
//
//var logMap map[string]logFun

func init() {
	//logFmtMap = make(map[string]logFmtFun)
	//logFmtMap[LevelTrace] = tlog.Debugf
	//logFmtMap[LevelDebug] = tlog.Debugf
	//logFmtMap[LevelInfo] = tlog.Infof
	//logFmtMap[LevelWarn] = tlog.Warnf
	//logFmtMap[LevelError] = tlog.Errorf
	//logFmtMap[LevelFatal] = tlog.Fatalf
	//
	//logMap = make(map[string]logFun)
	//logMap[LevelTrace] = tlog.Debug
	//logMap[LevelDebug] = tlog.Debug
	//logMap[LevelInfo] = tlog.Info
	//logMap[LevelWarn] = tlog.Warn
	//logMap[LevelError] = tlog.Error
	//logMap[LevelFatal] = tlog.Fatal
}
