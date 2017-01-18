//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package jvm;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class QMonitor {
    private static Map<String, MonitorItem> items = new ConcurrentHashMap();
    private static Map<String, AtomicLong> values = new ConcurrentHashMap();
    private static Map<String, MonitorItem> jvmItems = new ConcurrentHashMap();
    private static Map<String, Long> currentItems = new HashMap();
    private static Timer timer = new Timer("QMonitor");
    private static long lastUpdate;

    static {
        timer.schedule(new QMonitor.MonitorTask(), 0L, 2000L);
        lastUpdate = 0L;
    }

    public QMonitor() {
    }

    public static void recordOne(String name, long time) {
        recordMany(name, 1L, time);
    }

    public static void recordOne(String name) {
        recordMany(name, 1L, 0L);
    }

    public static void decrRecord(String name) {
        recordMany(name, -1L, 0L);
    }

    public static void recordMany(String name, long count, long time) {
        QMonitor.MonitorItem item = (QMonitor.MonitorItem)items.get(name);
        if(item == null) {
            item = new QMonitor.MonitorItem();
            items.put(name, item);
        }

        item.add(count, time);
    }

    public static void recordSize(String name, long size) {
        AtomicLong v = (AtomicLong)values.get(name);
        if(v == null) {
            v = new AtomicLong();
            values.put(name, v);
        }

        v.set(size);
    }

    public static void recordValue(String name, long count) {
        AtomicLong v = (AtomicLong)values.get(name);
        if(v == null) {
            v = new AtomicLong();
            values.put(name, v);
        }

        v.addAndGet(count);
    }

    public static Map<String, Long> getValues() {
        return currentItems;
    }

    private static class MonitorItem {
        private long count;
        private long time;

        private MonitorItem() {
        }

        public synchronized void add(long count, long time) {
            this.count += count;
            this.time += time;
        }

        public synchronized QMonitor.MonitorItem dumpAndClearItem() {
            QMonitor.MonitorItem item = new QMonitor.MonitorItem();
            item.count = this.count;
            item.time = this.time;
            this.count = 0L;
            this.time = 0L;
            return item;
        }
    }

    private static class MonitorTask extends TimerTask {
        private MonitorTask() {
        }

        public void run() {
            try {
                //每分钟的前10秒执行
                long current = System.currentTimeMillis();
                if(current - QMonitor.lastUpdate < 50000L) {
                    return;
                }

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(current);
                if(cal.get(13) > 10) {
                    return;
                }

                QMonitor.lastUpdate = current;
                HashMap ret = new HashMap();
                ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
                ret.put("JVM_Thread_Count", Long.valueOf((long)threadBean.getThreadCount()));
                List beans = ManagementFactory.getGarbageCollectorMXBeans();
                Iterator var8 = beans.iterator();

                String name;
                while(var8.hasNext()) {
                    GarbageCollectorMXBean entry = (GarbageCollectorMXBean)var8.next();
                    name = "JVM_" + entry.getName();
                    long item = entry.getCollectionCount();
                    long time = entry.getCollectionTime();
                    QMonitor.MonitorItem item1 = (QMonitor.MonitorItem)QMonitor.jvmItems.get(name);
                    if(item1 == null) {
                        item1 = new QMonitor.MonitorItem();
                        item1.add(item, time);
                        QMonitor.jvmItems.put(name, item1);
                    }

                    ret.put(this.makeName(name + "_Count"), Long.valueOf(item - item1.count));
                    if(item - item1.count > 0L) {
                        ret.put(this.makeName(name + "_Time"), Long.valueOf((time - item1.time) / (item - item1.count)));
                    }

                    item1 = new QMonitor.MonitorItem();
                    item1.add(item, time);
                    QMonitor.jvmItems.put(name, item1);
                }

                var8 = QMonitor.items.entrySet().iterator();

                Entry entry1;
                while(var8.hasNext()) {
                    entry1 = (Entry)var8.next();
                    name = (String)entry1.getKey();
                    QMonitor.MonitorItem item2 = ((QMonitor.MonitorItem)entry1.getValue()).dumpAndClearItem();
                    long count = item2.count;
                    long time1 = item2.time;
                    ret.put(this.makeName(name + "_Count"), Long.valueOf(count));
                    if(count > 0L) {
                        ret.put(this.makeName(name + "_Time"), Long.valueOf(time1 / count));
                    } else {
                        ret.put(this.makeName(name + "_Time"), Long.valueOf(0L));
                    }
                }

                var8 = QMonitor.values.entrySet().iterator();

                while(var8.hasNext()) {
                    entry1 = (Entry)var8.next();
                    ret.put(this.makeName((String)entry1.getKey() + "_Value"), Long.valueOf(((AtomicLong)entry1.getValue()).get()));
                }

                QMonitor.currentItems = Collections.unmodifiableMap(ret);
            } catch (Exception var15) {
                ;
            }

        }

        private String makeName(String name) {
            return name.replaceAll(" ", "_");
        }
    }
}
