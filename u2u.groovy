import org.cloud.sonic.agent.bridge.android.AndroidDeviceBridgeTool
import org.cloud.sonic.agent.common.models.HandleContext
import org.cloud.sonic.agent.tests.handlers.AndroidStepHandler
import org.cloud.sonic.driver.android.service.AndroidElement
import java.text.SimpleDateFormat
import java.util.Date
import java.security.SecureRandom

HandleContext handleContext = new HandleContext();
String packageName = "com.xxx"

(2..10).each { lineNumber ->
    androidStepHandler.log.sendStepLog(1, "运行", "第${lineNumber}次运行")

    // 从文本文件中获取下一行数据
    def nextLine = getNextLineFromTextFile(lineNumber)
    androidStepHandler.log.sendStepLog(1, "从文本文件中获取下一行数据", nextLine)

    // 调用performActions并传递nextLine作为参数
    performActions(androidStepHandler, handleContext, packageName, nextLine)
}

def performActions(androidStepHandler, handleContext,packageName,nextLine) {
    androidStepHandler.terminate(handleContext, packageName)
    androidStepHandler.appReset(handleContext, packageName)
    androidStepHandler.openApp(handleContext, packageName)

    androidStepHandler.log.sendStepLog(1, "导入钱包", "导入钱包")
    safeClick(androidStepHandler, "//android.widget.TextView[contains(@text,'Already have an account')]")
    Thread.sleep(3000)

    inputPwd()
    Thread.sleep(1500)

    inputPwd()
    Thread.sleep(2500)
    seed = androidStepHandler.setClipperByKeyboard(handleContext,nextLine)
    Thread.sleep(1000)

    androidStepHandler.log.sendStepLog(1, "点击复制", "点击复制助记词")
    safeClick(androidStepHandler, "//android.widget.TextView[@text='Paste']")
    Thread.sleep(2000)


    // 点击复制按钮
    safeClick(androidStepHandler, "//android.view.ViewGroup[@content-desc='Continue']")

    Thread.sleep(4000)

    inputPwd()
    Thread.sleep(3000)


    safeClick(androidStepHandler, "//android.widget.TextView[contains(@text,'Ecosystem')]")
    Thread.sleep(1500)

    safeClick(androidStepHandler, "(//android.widget.TextView[contains(@text,'Open')])[1]")
    Thread.sleep(1000)
    AndroidDeviceBridgeTool.executeCommand(androidStepHandler.iDevice, "input tap 922 136")

    Thread.sleep(5500)
    safeClick(androidStepHandler, "//android.widget.EditText[@resource-id='basic_referralCode']")
    Thread.sleep(2500)
    AndroidDeviceBridgeTool.executeCommand(androidStepHandler.iDevice, "input text 28829620")
    Thread.sleep(1000)

    safeClick(androidStepHandler, "//android.widget.TextView[@text='Connect Wallet']")


    Thread.sleep(1000)
    safeClick(androidStepHandler, "//android.widget.Button[contains(@text,'Submit')]")
    Thread.sleep(2500)

    (1..6).each { i ->
      AndroidDeviceBridgeTool.executeCommand(androidStepHandler.iDevice, "input swipe 540 2020 540 1640 500")
      Thread.sleep(250)
    }

    safeClick(androidStepHandler, "//android.widget.TextView[contains(@text,'Mint one of the 5 mystical NFTs')]")

    Thread.sleep(5500)

    (1..4).each { i ->
      AndroidDeviceBridgeTool.executeCommand(androidStepHandler.iDevice, "input swipe 540 2020 540 1640 500")
      Thread.sleep(500)
    }
    Thread.sleep(1500)

    AndroidDeviceBridgeTool.executeCommand(androidStepHandler.iDevice, "input tap 532 1666")
    Thread.sleep(2500)

    safeClick(androidStepHandler, "//android.widget.TextView[contains(@text,'Injected (Injected)')]")
    Thread.sleep(2500)

    try {
      def length = 8
      def characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
      def random = new SecureRandom()
      def randomString = (1..length).collect { characters[random.nextInt(characters.length())] }.join()
      
      androidStepHandler.log.sendStepLog(1, "随机生成的字符串", randomString)
      safeClick(androidStepHandler, "(android.widget.EditText)[1]")
      Thread.sleep(2500)
      AndroidDeviceBridgeTool.executeCommand(androidStepHandler.iDevice, "input text ${randomString}")
      Thread.sleep(1000)
  
      safeClick(androidStepHandler, "(android.widget.EditText)[2]")
  
      AndroidDeviceBridgeTool.executeCommand(androidStepHandler.iDevice, "input text ${randomString}@trx365.org")
      Thread.sleep(1000)
      AndroidDeviceBridgeTool.executeCommand(androidStepHandler.iDevice, "echo '${randomString}' >> /sdcard/email.txt")

        
      } catch (Exception e) {
            e.printStackTrace()
          
      }

    Thread.sleep(1000)

    safeClick(androidStepHandler, "//android.widget.CheckBox[@resource-id='accept']")
    Thread.sleep(1000)
    safeClick(androidStepHandler, "//android.widget.Button[contains(@text,'Finish sign-up')]")
    Thread.sleep(1000)
    safeClick(androidStepHandler, "//android.widget.Button[contains(@text,'Mint Now')]")
    Thread.sleep(1000)
    AndroidDeviceBridgeTool.executeCommand(androidStepHandler.iDevice, "input tap 540 1893")
    Thread.sleep(1000)

    safeClick(androidStepHandler, "//android.view.ViewGroup[contains(@content-desc,'Confirm')]")
    Thread.sleep(1000)
    safeClick(androidStepHandler, "//android.view.ViewGroup[contains(@content-desc,'Confirm')]")
    Thread.sleep(1000)

    Thread.sleep(4000)

    androidStepHandler.terminate(handleContext, packageName)
}

def getNextLineFromTextFile(lineNumber) {
    // 使用sed命令来获取文件的下一行数据
    def sedCommand = "sed -n '${lineNumber}p' /sdcard/new_example.txt"
    androidStepHandler.log.sendStepLog(1, "从文本文件中获取下一行数据的命令", sedCommand)

    def result = AndroidDeviceBridgeTool.executeCommand(androidStepHandler.iDevice, sedCommand)

    if (result != null && !result.isEmpty()) {
        // 获取命令执行结果中的文本内容
        def nextLine = result

        return nextLine
    } else {
        // 处理命令执行失败或结果为空的情况
        androidStepHandler.log.sendStepLog(1, "Error", "Failed to execute command: $sedCommand or empty result")
    }

    return null // 如果发生错误或文件为空，返回null或其他适当的值
}
    
def inputPwd(){
  (1..6).each { i ->
      AndroidDeviceBridgeTool.executeCommand(androidStepHandler.iDevice, "input text ${i}")
      Thread.sleep(250)
  }
}

def safeClick(androidStepHandler, String xpath) {
    def maxRetries = 2
    def retryCount = 0
    def length = 8
    def characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    def random = new SecureRandom()
    def randomString = (1..length).collect { characters[random.nextInt(characters.length())] }.join()
    
    while (retryCount < maxRetries) {
        try {
            AndroidElement element = androidStepHandler.findEle("xpath", xpath)
            element.click()
            Thread.sleep(1500)
            return // 点击成功，退出循环
        } catch (Exception e) {
            e.printStackTrace()
            retryCount++
            androidStepHandler.log.sendStepLog(1, "Error", "Click failed, retrying... (Attempt $retryCount)")
        }
    }

    androidStepHandler.log.sendStepLog(1, "Error", "Click failed after $maxRetries attempts")
}
