package utils

import (
	"path/filepath"
	"testing"

	"github.com/agiledragon/gomonkey"
	"github.com/stretchr/testify/assert"
	"github.com/stretchr/testify/suite"
)

func (s *fileTestSuite) TestFindFile() {

	filePath := FindFile(filepath.Join("file", "file.go"))
	assert.NotEqual(s.T(), "", filePath)
}

// 以下是需要初始化的声明
func TestFileTestSuite(t *testing.T) {
	suite.Run(t, new(fileTestSuite))
}

type fileTestSuite struct {
	suite.Suite
	mockPatches *gomonkey.Patches
}

func (s *fileTestSuite) SetupTest() {
	s.mockPatches = gomonkey.NewPatches()
}

func (s *fileTestSuite) TearDownSuite() {
	s.mockPatches.Reset()
}
