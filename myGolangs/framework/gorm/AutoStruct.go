package main

// GptStarConfig  星座预测
type GptStarConfig struct {
	ID         int64        `gorm:"primaryKey" gorm:"column:id" db:"id" json:"id" form:"id"`                                  //  主键
	Name       string       `gorm:"column:name" db:"name" json:"name" form:"name"`                                            //  配置名称
	Config     ConfigDetail `gorm:"serializer:json;column:config" db:"config" json:"config" form:"config"`                    //  配置信息
	IsDeleted  int64        `gorm:"column:is_deleted" db:"is_deleted" json:"is_deleted" form:"is_deleted"`                    //  是否删除,0否1是
	CreateTime int64        `gorm:"column:create_time;autoCreateTime" db:"create_time" json:"create_time" form:"create_time"` //  创建时间
	UpdateTime int64        `gorm:"column:update_time;autoUpdateTime" db:"update_time" json:"update_time" form:"update_time"` //  修改时间
}
type ConfigDetail struct {
	SignName string `json:"sign_name"`
	//Symbol        string   `json:"symbol"`
	//SignNameZh    string   `json:"sign_name_zh"`
	StartDate     string   `json:"start_date"`
	EndDate       string   `json:"end_date"`
	KeyTraits     []string `json:"key_traits"`
	Color         string   `json:"color"`
	Gem           string   `json:"gem"`
	Compatibility []string `json:"compatibility"`
	Personality   string   `json:"personality"`
	Work          string   `json:"work"`
	Love          string   `json:"love"`
	Friend        string   `json:"friend"`
	OrderBy       int      `json:"order_by"`
}
