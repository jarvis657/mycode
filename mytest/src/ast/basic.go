package main

import (
	"os"
	"runtime/trace"
	"strings"
)

type CollectRequest struct {
	Star int `form:"star" validation:"gte=1,lte=5" doc:"formData"`
}

var codeTemplate = `
func getReqStruct(r *http.Request) (*{{requestStructName}}, error) {
	r.ParseForm()
	var reqStruct = &{{requestStructName}}{}

	// bind data
	{{bindData}}j

	// bind partial example
	// reqStruct.{{fieldName}} =
	// {{transToFieldType}}(r.Form['{{fieldTagFormName}}'])


	if bindErr != nil {
		return nil, err
	}

	// validate data
	{{validateData}}

	// validate partial example
	// validateErr = validate(reqStruct.{{fieldName}}, validateStr)
	// if validateErr != nil
	// return nil, err


	return reqStruct, nil
}
`

func getTag(input string) []structTag {
	var out []structTag
	var tagStr = input
	tagStr = strings.Replace(tagStr, "`", "", -1)
	tagStr = strings.Replace(tagStr, "\"", "", -1)
	tagList := strings.Split(tagStr, " ")
	for _, val := range tagList {
		tmpArr := strings.Split(val, ":")
		st := structTag{}
		st.key = tmpArr[0]
		st.values = strings.Split(tmpArr[1], ",")
		out = append(out, st)
	}
	return out
}

type structTag struct {
	key    string
	values []string
}

func main() {
	// fset := token.NewFileSet()
	// // if the src parameter is nil, then will auto read the second filepath file
	// f, _ := parser.ParseFile(fset, "./example.go", nil, parser.Mode(0))
	// //	ast.Print(fset, f.Decls[0])

	// tagList := getTag(f.Decls[0].(*ast.GenDecl).Specs[0].(*ast.TypeSpec).Type.(*ast.StructType).Fields.List[0].Tag.Value)
	// fieldName := f.Decls[0].(*ast.GenDecl).Specs[0].(*ast.TypeSpec).Type.(*ast.StructType).Fields.List[0].Names[0].Name
	// fieldType := f.Decls[0].(*ast.GenDecl).Specs[0].(*ast.TypeSpec).Type.(*ast.StructType).Fields.List[0].Type.(*ast.Ident).Name
	// requestStructName := f.Decls[0].(*ast.GenDecl).Specs[0].(*ast.TypeSpec).Name.Name
	// fmt.Println(tagList)
	// fmt.Println(fieldName)
	// fmt.Println(fieldType)

	// exportFuncFile := "./ast/basic.go"
	// src, _ := ioutil.ReadFile(exportFuncFile)
	// // 创建一个文件集合，fset 类型是 token.FileSet
	// fset := token.NewFileSet()
	// // 得到 ast.File , 它视作一个文件语法树的根节点。被扫描的源代码文件会被加入到上面创建的文件集合中
	// f, _ := parser.ParseFile(fset, exportFuncFile, src, parser.ParseComments)
	// // 格式化打印语法树节点，即代码 2-2 的输出
	// ast.Print(fset, f) // fmt.Println(requestStructName)

	trace.Start(os.Stderr)
	defer trace.Stop()
	// create new channel of type int
	ch := make(chan int)

	// start new anonymous goroutine
	go func() {
		// send 42 to channel
		ch <- 42
	}()
	// read from channel
	<-ch

}
