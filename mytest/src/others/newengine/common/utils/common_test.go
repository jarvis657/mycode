package utils

import (
	"testing"
	"time"
)

func TestGetOptionalIp(t *testing.T) {
	tests := []struct {
		name string
		want string
	}{
		{
			name: "test",
			want: "10.64.70.75",
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			ip := GetOptionalIp()

			//uuid := GetUUID()
			t.Logf("%v got ip = %v",utils.CurrentTimestampToInt()), ip)
			if got := ip; got != tt.want {
				t.Errorf("GetOptionalIp() = %v, want %v", got, tt.want)
			}
		})
	}
}
