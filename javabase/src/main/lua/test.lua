--
-- Created by IntelliJ IDEA.
-- User: lmq
-- Date: 2020/4/3
-- Time: 19:50
-- To change this template use File | Settings | File Templates.
--

local tu="http://item.jd.com/73232432432.html"
s = tu:match('http://www.jd.com/product/(%d+).html') or tu:match('http://www.360buy.com/product/(%d+).html') or tu:match('item.jd.com/(%d+).html')
print("ok")
print(s)
print("===============================================================================")

local x = "x"
print(">>>"..x)
print(">>>#"..select('#', ...))
-- 打印参数
for i = 1, select('#', ...) do
    print(i..">>"..select(i, ...))
end


print("================================================================")

local tbtest = {
    [1] = 1,
    [2] = 2,
    [3] = 3,
    [4] = 4,
}

for key, value in pairs(tbtest) do
    print(value)
end

print("================================================================")
local tb1={1,2}
local tb2={3,4}
local x = table.insert(tb1,tb2)
local cjson=require("cjson")
print(cjson.encode(x))
print(cjson.encode(tb1))

print("================================================================")
local RedisQuery={
    {'sunionstore','tag_undefined'},
    {'sunionstore','ip_China'},
    {'sinter',"fds"},
    {'del',123},
    {'del',"fdd"},
}
local test={}
for i,req in ipairs(RedisQuery) do
    print(i.."<>"..tostring(table.concat(req,'')))
end
