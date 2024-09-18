package utils;

import base_domain.Resource;
import base_domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ExcelDataLoaderTest {

    private ExcelDataLoader excelDataLoader;

    @BeforeEach
    public void setUp() {
        String testFilePath = "data.xlsx";
        excelDataLoader = new ExcelDataLoader(testFilePath);
    }

    @Test
    public void testLoadResources() throws IOException {
        // 调用要测试的方法
        List<Resource> resources = excelDataLoader.loadResources();
        List<Task> tasks = excelDataLoader.loadTasks();

        // 验证数据正确性
        assertNotNull(resources, "Resource list should not be null");
        assertNotNull(tasks, "Resource list should not be null");


        // 验证具体的资源属性
        Resource resource = resources.getFirst();
        System.out.println(resource.getName());
    }
}