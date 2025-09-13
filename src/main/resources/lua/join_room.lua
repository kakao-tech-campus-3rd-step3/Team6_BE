-- KEYS[1] = participantsKey
-- KEYS[2] = metaKey
-- ARGV[1] = userId
-- ARGV[2] = userName
-- ARGV[3] = joinedAt (epoch seconds)

local clientStatus
local roomStatus
local data = nil

if redis.call("EXISTS", KEYS[2]) == 0 then
    clientStatus = "ROOM_NOT_FOUND"

elseif redis.call("HEXISTS", KEYS[1], ARGV[1]) == 1 then
    clientStatus = "ALREADY_JOINED"

else
    local capacity = tonumber(redis.call("HGET", KEYS[2], "capacity"))
    local participantsCount = tonumber(redis.call("HLEN", KEYS[1]))

    if participantsCount >= capacity then
        clientStatus = "FULL"
        roomStatus = "FULL"

    else
        -- 참가 처리
        local participant = cjson.encode({
            userId = tonumber(ARGV[1]),
            userName = ARGV[2],
            joinedAt = tonumber(ARGV[3]),
            role = "MEMBER"
        })
        redis.call("HSET", KEYS[1], ARGV[1], participant)
        clientStatus = "JOINED"

        if participantsCount + 1 >= capacity then
            roomStatus = "FULL"
        else
            roomStatus = "AVAILABLE"
        end

        -- meta 조회
        local metaRaw = redis.call("HGETALL", KEYS[2])
        local meta = {}
        for i = 1, #metaRaw, 2 do
            meta[metaRaw[i]] = metaRaw[i + 1]
        end
        meta.capacity = tonumber(meta.capacity)

        -- participants userId만 추출
        local rawParticipants = redis.call("HVALS", KEYS[1])
        local participantInfo = {}
        for i = 1, #rawParticipants do
            local p = cjson.decode(rawParticipants[i])
            table.insert(participantInfo, {
                id = p.userId,
                name = p.userName,
                role = p.role,
                joinedAt = p.joinedAt
            })
        end

        data = {
            status = roomStatus,
            room = meta,
            participants = participantInfo
        }
    end
end

return cjson.encode({ status = clientStatus, data = data })
