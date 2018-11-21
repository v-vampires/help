package script;

import com.google.common.collect.Lists;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

/**
 * Created by fitz.li on 2017/9/14.
 */
public class ScriptTest2 {
    public static void main(String[] args) throws FileNotFoundException, ScriptException, NoSuchMethodException {

        List<Price> prices = getPrices();
        List<Price> removePrice1 = Lists.newArrayList();
        //for (int i = 0; i < 100; i++) {
            for (Price price : prices) {
                long start = System.nanoTime();
                boolean isFilter = isFilter(price);
                System.out.println("java cost:" + price.getWrapperId() + ":" + (System.nanoTime() - start));
                if(isFilter){
                    removePrice1.add(price);
                }
            }
        //}

        ScriptEngineManager sm = new ScriptEngineManager();

        NashornScriptEngineFactory factory = null;
        for (ScriptEngineFactory f : sm.getEngineFactories()) {
            if (f.getEngineName().equalsIgnoreCase("Oracle Nashorn")) {
                factory = (NashornScriptEngineFactory)f;
                break;
            }
        }

        String[] stringArray = new String[]{"-doe", "--global-per-engine"};
        ScriptEngine engine = factory.getScriptEngine(stringArray);
        //ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        final CompiledScript compiled = ((Compilable)engine).compile(new FileReader("C:\\Users\\fitz.li.QUNARSERVERS\\IdeaProjects\\help\\help-common\\src\\main\\java\\script\\script.js"));
        compiled.eval();
        Invocable invocable = (Invocable) engine;
        List<Price> removePrice2 = Lists.newArrayList();

        //for (int i = 0; i < 100; i++) {
            for (Price price : prices) {
                long start = System.nanoTime();
                Boolean result = (Boolean) invocable.invokeFunction("jsFilter", price);
                System.out.println("js cost:" + price.getWrapperId() + ":" + (System.nanoTime() - start));
                if(result){
                    System.out.println("js filter result! price:" + price.getFlightNo()+",result.getClass:" + result.getClass());
                    removePrice2.add(price);
                }
            }
        //}
        //System.out.println("js cost:" + (System.currentTimeMillis() - start));

        System.out.println(removePrice1.size() == removePrice2.size());

        System.out.println("---------------------------");

    }

    public static List<Price> getPrices(){
        List<Price> prices = Lists.newArrayList();
        for (int i = 1; i <= 10; i++) {
            prices.add(new Price("flightNo"+i, "wrapper"+ i, i));
        }
        return prices;
    }

    public static boolean isFilter(Price price){
        if(price.getPrice() == 5){
            return true;
        }
        return false;
    }

    public static boolean wrapperFilter4JS(Price price){
        return price.getWrapperId().equals("wrapper5");
    }

    public boolean flightNoFilter4JS(Price price){

        return price.getFlightNo().equals("flightNo6");
    }
}
