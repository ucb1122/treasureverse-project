package com.treasureverse.backend.auth.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.treasureverse.backend.global.exception.BusinessException;
import com.treasureverse.backend.global.exception.ErrorCode;
import com.treasureverse.backend.member.domain.SocialLoginTicket;
import java.time.Duration;
import java.util.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SocialLoginTicketStore {
  private static final Duration TICKET_TTL = Duration.ofMinutes(10);
  private static final String KEY_PREFIX = "auth:social-ticket:";

  private final StringRedisTemplate redis;
  private final ObjectMapper objectMapper;

  public SocialLoginTicketStore(StringRedisTemplate redis, ObjectMapper objectMapper) {
    this.redis = redis;
    this.objectMapper = objectMapper;
  }

  public String create(SocialLoginTicket ticket) {
    String ticketId = UUID.randomUUID().toString();
    try {
      redis
          .opsForValue()
          .set(KEY_PREFIX + ticketId, objectMapper.writeValueAsString(ticket), TICKET_TTL);
      return ticketId;
    } catch (JsonProcessingException exception) {
      throw new IllegalStateException("소셜 로그인 정보를 저장하지 못했습니다.", exception);
    }
  }

  public SocialLoginTicket consume(String ticketId) {
    String serialized = redis.opsForValue().getAndDelete(KEY_PREFIX + ticketId);
    if (serialized == null) {
      throw new BusinessException(ErrorCode.INVALID_REQUEST);
    }
    try {
      return objectMapper.readValue(serialized, SocialLoginTicket.class);
    } catch (JsonProcessingException exception) {
      throw new BusinessException(ErrorCode.INVALID_REQUEST);
    }
  }
}
