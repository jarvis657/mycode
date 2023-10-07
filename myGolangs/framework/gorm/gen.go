package main

import (
	"database/sql"

	"gorm.io/driver/mysql"
	"gorm.io/gen"
	"gorm.io/gorm"
)

// Dynamic SQL
type Querier interface {
	// SELECT * FROM @@table WHERE name = @name{{if role !=""}} AND role = @role{{end}}
	FilterWithNameAndRole(name, role string) ([]gen.T, error)
}

func main() {
	// specify the output directory (default: "./query")
	// ### if you want to query without context constrain, set mode gen.WithoutContext ###
	g := gen.NewGenerator(gen.Config{
		OutPath: "./dal/query",
		/* Mode: gen.WithoutContext|gen.WithDefaultQuery*/
		//if you want the nullable field generation property to be pointer type, set FieldNullable true
		/* FieldNullable: true,*/
		//if you want to generate index tags from database, set FieldWithIndexTag true
		/* FieldWithIndexTag: true,*/
		//if you want to generate type tags from database, set FieldWithTypeTag true
		/* FieldWithTypeTag: true,*/
		//if you need unit tests for query code, set WithUnitTest true
		/* WithUnitTest: true, */
	})

	// reuse the database connection in Project or create a connection here
	// if you want to use GenerateModel/GenerateModelAs, UseDB is necessray or it will panic
	// db, _ := gorm.Open(mysql.Open("root:@(127.0.0.1:3306)/demo?charset=utf8mb4&parseTime=True&loc=Local"))
	db, _ := gorm.Open(mysql.Open("root:zysoft.COM@(127.0.0.1:3306)/test?charset=utf8mb4&parseTime=True&loc=Local"))
	g.UseDB(db)

	// apply basic crud api on structs or table models which is specified by table name with function
	// GenerateModel/GenerateModelAs. And generator will generate table models' code when calling Excute.
	g.ApplyBasic(g.GenerateModel("sys_region", gen.FieldIgnore("test_datetime")))
	//g.ApplyBasic(g.GenerateModel("sys_region"), g.GenerateModelAs("people", "Person", gen.FieldIgnore("test_datetime")))

	// apply diy interfaces on structs or table models
	//g.ApplyInterface(func(method model.Method) {}, g.GenerateModel("company"))

	// execute the action of code generation
	g.Execute()
}

// 地区表
type SysRegion struct {
	Id         int            `gorm:"column:id;type:int(11);AUTO_INCREMENT;comment:地区主键编号;primary_key" json:"id"`
	Name       sql.NullString `gorm:"column:name;type:varchar(50);comment:地区名称" json:"name"`
	ShortName  sql.NullString `gorm:"column:short_name;type:varchar(50);comment:简称" json:"short_name"`
	Code       sql.NullString `gorm:"column:code;type:varchar(50);comment:行政地区编号" json:"code"`
	ParentCode sql.NullString `gorm:"column:parent_code;type:varchar(50);comment:父id" json:"parent_code"`
	Level      sql.NullInt32  `gorm:"column:level;type:int(11);comment:1级：省、直辖市、自治区
2级：地级市
3级：市辖区、县（旗）、县级市、自治县（自治旗）、特区、林区
4级：镇、乡、民族乡、县辖区、街道
5级：村、居委会" json:"level"`
	Flag         sql.NullInt32 `gorm:"column:flag;type:int(11);comment:0:正常 1废弃" json:"flag"`
	TestDatetime sql.NullTime  `gorm:"column:test_datetime;type:datetime" json:"test_datetime"`
}

func (m *SysRegion) TableName() string {
	return "sys_region"
}
