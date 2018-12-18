package designPattern.filter;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by fitz.li on 2018/12/17.
 * 责任链实现方式1：构造Chain对象。通过调用chain.doFilter方法来递归调用
 * spring aop以及web的Filter 都是采用这种方式来实现的
 */
public class MainClass1 {
    interface Filter{
        void doFilter(String req, String res, FilterChain chain);
    }

    static class FilterChain{

        int index = 0;
        List<Filter> filters = Lists.newArrayList();

        public void addFilter(Filter f){
            filters.add(f);
        }

        public void doFilter(String req, String res) {
            if(index == filters.size()){
                return;
            }
            Filter filter = filters.get(index++);
            filter.doFilter(req, res, this);
        }
    }

    static class Filter1 implements Filter{

        @Override
        public void doFilter(String req, String res, FilterChain chain) {
            System.out.println("Filter1 before");
            chain.doFilter(req, res);
            System.out.println("Filter1 end");
        }
    }

    static class Filter2 implements Filter{

        @Override
        public void doFilter(String req, String res, FilterChain chain) {
            System.out.println("Filter2 before");
            chain.doFilter(req, res);
            System.out.println("Filter2 end");
        }
    }

    public static void main(String[] args) {
        FilterChain chain = new FilterChain();
        chain.addFilter(new Filter1());
        chain.addFilter(new Filter2());
        chain.doFilter("req", "res");

    }
}
