package main

import (
	"reflect"
	"testing"
)

func Test_receiveEvent(t *testing.T) {
	type args struct {
		t []string
	}
	tests := []struct {
		name string
		args args
		want [][2]int32
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := receiveEvent(tt.args.t); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("receiveEvent() = %v, want %v", got, tt.want)
			}
		})
	}
}

func Test_initCar(t *testing.T) {
	type args struct {
		car Car
		x   int32
		y   int32
	}
	tests := []struct {
		name string
		args args
		want *Car
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := initCar(tt.args.car, tt.args.x, tt.args.y); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("initCar() = %v, want %v", got, tt.want)
			}
		})
	}
}

func Test_moveCar(t *testing.T) {
	type args struct {
		car  Car
		move [2]int32
	}
	tests := []struct {
		name string
		args args
		want *Car
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := moveCar(tt.args.car, tt.args.move); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("moveCar() = %v, want %v", got, tt.want)
			}
		})
	}
}

func Test_info(t *testing.T) {
	type args struct {
		car Car
	}
	tests := []struct {
		name string
		args args
		want *[2]int32
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := info(tt.args.car); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("info() = %v, want %v", got, tt.want)
			}
		})
	}
}

func Test_turn(t *testing.T) {
	type args struct {
		t string
	}
	tests := []struct {
		name string
		args args
		want *[2]int32
	}{
		// TODO: Add test cases.
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			if got := turn(tt.args.t); !reflect.DeepEqual(got, tt.want) {
				t.Errorf("turn() = %v, want %v", got, tt.want)
			}
		})
	}
}
