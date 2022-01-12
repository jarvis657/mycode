// Copyright (c) 2015-Tencent, Inc. All Rights Reserved.
// See License.txt for license information.

package utils

import (
	"os"
	"path/filepath"
	"strings"
)

var (
	commonBaseSearchPaths = []string{
		".",
		"..",
		"../..",
		"../../..",
	}
)

func FindPath(path string, baseSearchPaths []string, filter func(os.FileInfo) bool) string {

	if !filepath.IsAbs(path) {
		return findPath(path, baseSearchPaths, filter)
	}
	if _, err := os.Stat(path); err == nil {
		return path
	}

	return ""
}

func findPath(path string, baseSearchPaths []string, filter func(os.FileInfo) bool) string {

	searchPaths := parseSearchPaths(baseSearchPaths)
	for _, parent := range searchPaths {
		found, err := filepath.Abs(filepath.Join(parent, path))
		if err != nil {
			continue
		}
		if isStat(found, filter) {
			return found
		}
	}

	return ""
}

func isStat(found string, filter func(os.FileInfo) bool) bool {

	if fileInfo, err := os.Stat(found); err == nil {
		if filter == nil {
			return true
		}
		if filter(fileInfo) {
			return true
		}
	}
	return false
}

// Additionally attempt to search relative to the location of the running binary.
func parseSearchPaths(baseSearchPaths []string) []string {

	searchPaths := make([]string, 0)
	searchPaths = append(searchPaths, baseSearchPaths...)

	if binaryDir, err := parseBinaryDir(); err == nil && binaryDir != "" {
		for _, baseSearchPath := range baseSearchPaths {
			searchPaths = append(searchPaths, filepath.Join(binaryDir, baseSearchPath))
		}
	}

	return searchPaths
}

func parseBinaryDir() (string, error) {

	var exe string
	var err error

	if exe, err = os.Executable(); err != nil {
		return "", err
	}

	if exe, err = filepath.EvalSymlinks(exe); err != nil {
		return "", err
	}

	if exe, err = filepath.Abs(exe); err != nil {
		return "", err
	}

	return filepath.Dir(exe), nil
}

// FindFile looks for the given file in nearby ancestors relative to the current working
// directory as well as the directory of the executable.
func FindFile(path string) string {
	return FindPath(path, commonBaseSearchPaths, func(fileInfo os.FileInfo) bool {
		return !fileInfo.IsDir()
	})
}

func GetBasePath() (string, error) {
	path, err := os.Getwd()
	if err != nil {
		return "", err
	}

	paths := strings.Split(path, "qqcd")
	return paths[0], nil
}
