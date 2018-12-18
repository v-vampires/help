package designPattern.filter;

/**
 * Created by fitz.li on 2018/12/17.
 * 责任链实现方式2：设置每一个filter的next节点，先执行自己的逻辑，再执行next的逻辑
 */
public class MainClass2 {
    interface Filter{
        void doFilter(String req, String res);
    }

    static class Filter1 implements Filter{

        private Filter next;

        public Filter1(Filter next) {
            this.next = next;
        }

        @Override
        public void doFilter(String req, String res) {
            System.out.println("Filter1 before");
            if(next != null){
                next.doFilter(req, res);
            }
            System.out.println("Filter1 end");
        }
    }

    static class Filter2 implements Filter{

        private Filter next;

        public Filter2(Filter next) {
            this.next = next;
        }
        @Override
        public void doFilter(String req, String res) {
            System.out.println("Filter2 before");
            if(next!= null){
                next.doFilter(req, res);
            }
            System.out.println("Filter2 end");
        }
    }

    public static void main(String[] args) {
        Filter filter = new Filter1(new Filter2(null));
        filter.doFilter("req", "res");
    }
}
