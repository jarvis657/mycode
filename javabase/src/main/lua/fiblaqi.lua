fib = coroutine.create(
    function ()
        local x, y = 1, 1
        while true do
            coroutine.yield(x)
            x, y = x+y, x
        end
    end)
local b,r
function main()
    for i = 1, 10 do
        b,r = coroutine.resume(fib)
    end
end
local result= pcall(main)
print(b,r)
