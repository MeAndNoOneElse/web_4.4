package service;

import dto.PointRequest;
import dto.ResultResponse;
import entity.Result;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ResultRepository;
import repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    public ResultResponse checkAndSave(String username, PointRequest request) {
        long start = System.nanoTime();

        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found")); // <<< использование репозитория

        boolean hit = checkHit(request.getX(), request.getY(), request.getR());
        LocalDateTime now = LocalDateTime.now();
        long execTime = System.nanoTime() - start;

        Result result = new Result(
                null,
                request.getX(),
                request.getY(),
                request.getR(),
                hit,
                now,
                execTime,
                user
        );

        resultRepository.save(result); // <<< использование репозитория

        return new ResultResponse(
                result.getId(),
                result.getX(),
                result.getY(),
                result.getR(),
                result.isHit(),
                result.getTimestamp(),
                result.getExecutionTime()
        );
    }

    public List<ResultResponse> getUserResults(String username) {
        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // метод из твоего ResultRepository: List findByUserOrderByTimestampDesc(User user)
        List<Result> results = resultRepository.findByUserOrderByTimestampDesc(user);

        return results.stream()
                .map(r -> new ResultResponse(
                        r.getId(),
                        r.getX(),
                        r.getY(),
                        r.getR(),
                        r.isHit(),
                        r.getTimestamp(),
                        r.getExecutionTime()
                ))
                .collect(Collectors.toList());
    }

    public void clearUserResults(String username) {
        User user = (User) userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // метод deleteByUser(User user)
        resultRepository.deleteByUser(user);
    }

    private boolean checkHit(Double x, Double y, Double r) {
        if (x == null || y == null || r == null) {
            return false;
        }

        // Пример логики; можешь подставить свою
        if (x >= 0 && y >= 0) {
            return x * x + y * y <= r * r;
        }
        if (x <= 0 && y >= 0) {
            return x >= -r && y <= r;
        }
        if (x <= 0 && y <= 0) {
            return y >= -x - r;
        }
        return false;
    }
}
