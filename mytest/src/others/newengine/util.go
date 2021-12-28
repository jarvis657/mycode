package newfsmengine

import "fmt"

func taskSubId(taskId interface{}, subId interface{}) string {
	return fmt.Sprintf("%v_%v", taskId, subId)
}
func taskSubIdBn(taskId interface{}, subId interface{}, batchNum interface{}) string {
	return fmt.Sprintf("%v_%v_%v", taskId, subId, batchNum)
}

func extractDbIds(s *releaseEngineServiceImpl) []int32 {
	tsids := make([]int32, 0)
	//taskId_subtaskid value:memTaskDoing
	s.holdMemTaskDoingMap.Range(func(key, mtd interface{}) bool {
		tsids = append(tsids, mtd.(*memTaskDoing).taskDbId)
		return true
	})
	return tsids
}
func check(mem *memTaskDoing)  {

}
