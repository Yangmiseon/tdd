package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@Service
public class PointService {
    private static final Logger log = LoggerFactory.getLogger(PointService.class);
    //객체주입
    private final PointHistoryTable pointHistoryTable;
    private final UserPointTable userPointTable;

    //생성자 작성
    public PointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    // 특정 유저의 포인트 조회
    public UserPoint getUserPoint(long userId) {

        return userPointTable.selectById(userId);
    }

    //특정유저의 포인트 내역 조회
    public List<PointHistory> getPointHistory(long userId) {

        return pointHistoryTable.selectAllByUserId(userId);
    }

    //특정유저의 포인트 충전
    public UserPoint chargeUserPoint(long userId, long amount) {
        //현재 포인트 조회
        long point = userPointTable.selectById(userId).point();
        //현재 포인트와 충전포인트더하기
        amount += point;
        //히스토리에 내역 남기기
        pointHistoryTable.insert(userId, amount, TransactionType.CHARGE, System.currentTimeMillis());
        //테이블에 유저아이디와 충전되어야 하는 값 넣어주기
        return userPointTable.insertOrUpdate(userId, amount);
    }

    //특정유저의 포인트 사용
    public UserPoint useUserPoint(long userId, long amount) {
        //현재 포인트 조회
        long point = userPointTable.selectById(userId).point();
        log.info("처음: userId={}, 보유={}, 요청={}", userId, point, amount);
        //현재 포인트와 사용포인트빼기

        if(point < amount) {
            throw new RuntimeException();
        }else{
            amount = point-amount;
            log.info("차감 후 포인트: {}", amount);
            //히스토리에 내역 남기기
            pointHistoryTable.insert(userId, amount, TransactionType.USE, System.currentTimeMillis());
            //테이블에 유저아이디와 충전되어야 하는 값 넣어주기
            return userPointTable.insertOrUpdate(userId, amount);
        }
    }

}

