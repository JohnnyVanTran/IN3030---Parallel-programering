import java.util.LinkedList;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class FactorMonitor {

    private final Lock lock = new ReentrantLock(true);
    private final Condition wait_for_work = lock.newCondition();
    public long[] to_factor; 
    public Long current_base;
    public int factor_index;
    // public long current_to_factor;
    public TreeMap<Long, LinkedList<Long>> result_bucket;
    public boolean started = false;
    public boolean dead_end = true;
    public int total_threads= 0;
    public int working_threads = 0;
    public int threads_exausted = 0;


    public FactorMonitor(long[] to_factor, TreeMap<Long, LinkedList<Long>> result_bucket){
        this.to_factor = to_factor;      
        this.factor_index = 0;
        this.result_bucket = result_bucket;
        this.current_base = to_factor[factor_index];
    }

    public long get_work(long prev){
        lock.lock();
        try{
            // working_threads --;
            if(!this.started){
                set_next_task();
                this.started = true;
            }

            return this.current_base;
        }
        finally{
            lock.unlock();
        }

    }

    public void add_factor(long base, ArrayList<Long> factors, int id){
        lock.lock();
        try{
            working_threads --;
            if(working_threads == 0){
                if (factors.size() > 0) {
                    for (int i = 0; i < factors.size(); i++) {
                        this.result_bucket.get(current_base).add(factors.get(i));
                    }
                }
                long rest = current_base;
                for (Long factor : this.result_bucket.get(current_base)) {
                    rest /= factor;
                }
                if(rest > 1){
                    this.result_bucket.get(current_base).add(rest);
                }
                set_next_task();
                working_threads = total_threads;
                wait_for_work.signalAll();
            }
            else{
                if(factors.size() > 0) {
                    for (int i = 0; i < factors.size(); i++) {
                        this.result_bucket.get(current_base).add(factors.get(i));
                    }
                }
                try {
                    wait_for_work.await();
                } catch (Exception e) {}
            }
        }
        finally{
            lock.unlock();
        }
    }

    private void set_next_task(){
        if(this.factor_index == this.to_factor.length){
            this.current_base = (long) 0;
            return;
        }
        this.current_base = this.to_factor[this.factor_index];
        this.factor_index ++;
    } 
}