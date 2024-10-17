//package Utils;
//
//import DataStruct.Operation;
//import DataStruct.Resource.RenewableResource;
//import DataStruct.Task;
//import DataStruct.TaskType;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//
//public class ExcelDataLoader {
//    private final String dataFilePath;
//
//    public ExcelDataLoader(String dataFilePath) {
//        this.dataFilePath = dataFilePath;
//    }
//
//    public List<RenewableResource> loadResources() throws IOException {
//        List<RenewableResource> resourceList = new ArrayList<>();
//        FileInputStream file = new FileInputStream(this.dataFilePath);
//        Workbook workbook = new XSSFWorkbook(file);
//        Sheet sheet = workbook.getSheet("设备组清单");
//        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//            RenewableResource resource = new RenewableResource();
//            resource.setId(String.valueOf(i));
//            resource.setName(sheet.getRow(i).getCell(1).getStringCellValue());
//
//            resourceList.add(resource);
//        }
//        return resourceList;
//    }
//
//    public List<Task> loadTasks() throws IOException {
//        List<Resource1> resource1List = this.loadResources();
//        List<Task> taskList = new ArrayList<>();
//
//        FileInputStream file = new FileInputStream(this.dataFilePath);
//        Workbook workbook = new XSSFWorkbook(file);
//        Sheet taskSheet = workbook.getSheet("作业单");
//        Sheet craftSheet = workbook.getSheet("产品工艺");
//
//        // 构建任务
//        for (int i = 1; i <= taskSheet.getLastRowNum(); i++) {
//            Task task = new Task();
//            task.setId(taskSheet.getRow(i).getCell(1).getStringCellValue());
//            task.setTaskType(TaskType.PARENT);
//            task.setDueDate(taskSheet.getRow(i).getCell(9).getLocalDateTimeCellValue());
//            task.setMaterialId(taskSheet.getRow(i).getCell(3).getStringCellValue());
//            task.setMaterialDescription(taskSheet.getRow(i).getCell(4).getStringCellValue());
//            task.setUnclearedQuantity((int) taskSheet.getRow(i).getCell(5).getNumericCellValue());
//
//            String materialCode = task.getMaterialId();
//
//            // 构建task的工艺路径
//            List<Operation> craftPath = new ArrayList<>();
//            for (int j = 1; j <= craftSheet.getLastRowNum(); j++) {  // 遍历工艺表
//                Row craftRow = craftSheet.getRow(j);
//                if(craftRow.getCell(1).getStringCellValue().equals(materialCode)){
//                    Operation operation = new Operation();
//                    operation.setId(RandomStringGenerator.generateRandomString(16));
//                    operation.setName(craftRow.getCell(4).getStringCellValue());
//                    operation.setOrder(Integer.parseInt(craftRow.getCell(5).getStringCellValue()));
//                    operation.setMinutesPerBeat((float) craftRow.getCell(10).getNumericCellValue());
//                    operation.setMinutesPerBeat((float) craftRow.getCell(9).getNumericCellValue());
//                    operation.setParentTask(task);
//                    if (!craftPath.isEmpty()) {
//                        operation.setPreviousOperation(craftPath.getLast());
//                    }
//
//                    // 构建可用资源
//                    List<Resource1> operationResource1s = new ArrayList<>();
//                    String availableResourceCodes = craftRow.getCell(7).getStringCellValue().trim();
//                    String[] resourceCodesArray = availableResourceCodes.split("，");
//                    for(String code: resourceCodesArray){
//                        resource1List.stream().filter(r -> r.getId().equals(code)).findFirst().ifPresent(operationResource1s::add);
//                    }
//                    operation.setAvailableResources(operationResource1s);
//                    craftPath.add(operation);
//                }
//            }
//            // 对工艺路径按照order排序
//            craftPath.sort(Comparator.comparing(Operation::getOrder));
//            task.setCraftPath(craftPath);
//            taskList.add(task);
//        }
//        return taskList;
//    }
//}
