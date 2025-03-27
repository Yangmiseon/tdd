package io.hhplus.tdd.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {


        return new UserPoint(id, 0, System.currentTimeMillis());
    }
}

//record는 getter 알아서 생성해줌 - setter는 없음 불변임
//DTO(요청/응답용)
//읽기 전용 데이터 모델
//테스트용 데이터 구조에 사용하기 좋음
