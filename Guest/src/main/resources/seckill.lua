-- ! 参数列表

-- 优惠卷id
local voucherId = ARGV[1]
-- 用户id
local userId = ARGV[2]

-- ! 数据key

-- 库存key
local stockKey = 'seckill:stock:' .. voucherId
-- 订单key
local orderKey = 'seckill:order:' .. voucherId

-- ! 鉴权

-- 判断库存是否充足
if (tonumber(redis.call('get', stockKey)) <= 0) then
    return 1     -- 库存不足 返回1
end

-- 判断用户是否下单
if(redis.call('sismember',orderKey,userId) == 1) then
    return 2     -- 重复下单 返回2
end

-- ! 业务逻辑

-- 扣库存
redis.call('incrby',stockKey,-1)
-- 下单并保存用户
redis.call('sadd',orderKey,userId)

return 0
