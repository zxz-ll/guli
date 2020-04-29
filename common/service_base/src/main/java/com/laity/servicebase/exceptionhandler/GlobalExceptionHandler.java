package com.laity.servicebase.exceptionhandler;



import com.laity.commonutils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //指定出现什么异常执行这个方法
    @ExceptionHandler(Exception.class) //注解是异常处理器，后边的参数是设置什么类型的错误会进行提示
    @ResponseBody //为了返回数据
    public R error(Exception e) {
        e.printStackTrace();
        return R.error().message("执行了全局异常处理..");
    }

    //特定异常
    @ExceptionHandler(NullPointerException.class) //注解是异常处理器，后边的参数是设置什么类型的错误会进行提示
    @ResponseBody //为了返回数据
    public R error(NullPointerException e) {
        e.printStackTrace();
        return R.error().message("空指针异常..");
    }

    //上面两个都是系统自己输出的异常
    //自定义异常   要在方法中手动输出
    //指定出现什么异常执行这个方法
    @ExceptionHandler(LaityException.class) //注解是异常处理器，后边的参数是设置什么类型的错误会进行提示
    @ResponseBody //为了返回数据
    public R error(LaityException e) {
        log.error(e.getMsg()); //  Slf4j用法 将异常输出到error级别的异常文件（Logback定义路径）中去
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }

    //快捷键【CTRL+ALT+T】
/*    public void testException(){
        try {
            int i=10/0;
        } catch (Exception e) {
           //执行自定义异常
            throw new LaityException(2001," 饿出异常了");
        }
    }*/


}
