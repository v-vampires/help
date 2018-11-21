var jsFilter = function (price) {
    if(priceFilter(price) || wrapperFilter(price) || flightNoFilter(price)){
        //访问属性
        print('price.flightNo:' + price.flightNo + ',price.wrapperId:'+ price.wrapperId);
        //访问方法
        price.doSomething(price.price);
        return true;
    }
    return false;
};
/**
 * js判断
 * @param price
 * @returns {boolean}
 */
var priceFilter = function (price) {
    return (price.price>=10);
}

//引入java类
var ScriptTest = Java.type('script.ScriptTest')
/**
 * js调用java静态方法判断
 * @param price
 * @returns {boolean}
 */
var wrapperFilter = function (price) {
    return ScriptTest.wrapperFilter4JS(price);
}
/**
 * js调用java实例方法判断
 * @param price
 * @returns {boolean}
 */
var flightNoFilter = function (price) {
    var st = new ScriptTest();
    return st.flightNoFilter4JS(price);
}