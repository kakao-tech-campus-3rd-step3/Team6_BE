-- KEYS[1] = participantsKey (hash)
-- KEYS[2] = metaKey
-- ARGV[1] = roomId
-- ARGV[2] = roomName
-- ARGV[3] = capacity
-- ARGV[4] = userId
-- ARGV[5] = userName
-- ARGV[6] = joinedAt (epoch seconds)

local status

-- 이미 존재하는 방이면 중단
if redis.call("EXISTS", KEYS[2]) == 1 then
    status = "ROOM_ALREADY_EXISTS"
else
    -- 메타 저장
    redis.call("HSET", KEYS[2],
            "roomId", ARGV[1],
            "name", ARGV[2],
            "capacity", ARGV[3]
    )

    -- 첫 참여자 추가 (Hash: userId -> JSON)
    local participant = cjson.encode({
        userId = tonumber(ARGV[4]),
        userName = ARGV[5],
        joinedAt = tonumber(ARGV[6])
    })
    redis.call("HSET", KEYS[1], ARGV[4], participant)

    status = "CREATED"
end

return cjson.encode({ status = status })
