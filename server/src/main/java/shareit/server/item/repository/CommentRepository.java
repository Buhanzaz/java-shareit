package shareit.server.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shareit.server.item.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByItem_Id(Long itemId);

    List<Comment> findByItem_UserId(Long userId);
}
