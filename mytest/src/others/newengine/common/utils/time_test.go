package utils

import "testing"

func TestCurrentTimestampToInt(t *testing.T) {
	tests := []struct {
		name string
		want int32
	}{
		// TODO: Add test cases.
		{
			name: "t1",
			want: 12,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := CurrentTimestampToInt(); got != tt.want {
				t.Errorf("CurrentTimestampToInt() = %v, want %v", got, tt.want)
			} else {
				t.Errorf("CurrentTimestampToInt() = %v, want %v", got, tt.want)
			}

		})
	}
}
