package utils

func CheckElementInSlice(s []string, b string) bool {
	for _, v := range s {
		if b == v {
			return true

		}
	}
	return false
}
