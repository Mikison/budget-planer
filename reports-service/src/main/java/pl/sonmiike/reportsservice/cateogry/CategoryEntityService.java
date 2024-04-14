package pl.sonmiike.reportsservice.cateogry;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryEntityService {

    private final CategoryEntityRepository categoryEntityRepository;

    public List<CategoryEntity> getCategories() {
        return categoryEntityRepository.findAll();
    }
}
