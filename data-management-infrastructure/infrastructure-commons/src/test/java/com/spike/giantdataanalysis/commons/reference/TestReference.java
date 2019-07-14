package com.spike.giantdataanalysis.commons.reference;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import org.junit.Test;

import com.spike.giantdataanalysis.commons.annotation.constraint.PreCondition;

/**
 * @see java.lang.ref.SoftReference
 * @author zhoujiagen
 */
public class TestReference {
  /**
   * <b>N</b> is an experience value, should change
   * <p>
   * Constraint: NO OutOfMemoryError: Java heap space
   * </p>
   */
  private static final void triggerGC() {
    int N = 1670000;
    @SuppressWarnings("unused")
    int[] array = new int[N];
  }

  @PreCondition(description = "run setting: Oracle HotSpot JVM, -Xms10m -Xmx10m")
  @Test
  public void soft() {
    ExampleReferenceBean ExampleReferenceBean =
        new ExampleReferenceBean("java.lang.ref.SoftReference");
    ReferenceQueue<ExampleReferenceBean> referenceQueue =
        new ReferenceQueue<ExampleReferenceBean>();
    SoftReference<ExampleReferenceBean> softReference =
        new SoftReference<ExampleReferenceBean>(ExampleReferenceBean, referenceQueue);
    System.out.println(softReference.get());

    ExampleReferenceBean = null;
    assertNotNull(softReference.get());

    System.gc();
    assertNotNull(softReference.get());

    triggerGC();
    assertNull(softReference.get());// now null

    Reference<? extends ExampleReferenceBean> reference = referenceQueue.poll();
    assertNotNull(reference);// still can get the reference
    System.out.println(reference.get());

    triggerGC();
    reference = referenceQueue.poll();
    assertNull(reference);// now reference is null
  }

  @Test
  public void weak() {
    ExampleReferenceBean ExampleReferenceBean =
        new ExampleReferenceBean("java.lang.ref.WeakReference");
    ReferenceQueue<ExampleReferenceBean> referenceQueue =
        new ReferenceQueue<ExampleReferenceBean>();
    WeakReference<ExampleReferenceBean> weakReference =
        new WeakReference<ExampleReferenceBean>(ExampleReferenceBean, referenceQueue);
    System.out.println(weakReference.get());

    ExampleReferenceBean = null;
    assertNotNull(weakReference.get());

    System.gc();
    assertNull(weakReference.get());// now null

    Reference<? extends ExampleReferenceBean> reference = referenceQueue.poll();
    assertNotNull(reference);
    assertNull(reference.get());
  }

  @Test
  public void phantom() {
    ExampleReferenceBean ExampleReferenceBean =
        new ExampleReferenceBean("java.lang.ref.PhantomReference");
    ReferenceQueue<ExampleReferenceBean> referenceQueue =
        new ReferenceQueue<ExampleReferenceBean>();
    PhantomReference<ExampleReferenceBean> phantomReference =
        new PhantomReference<ExampleReferenceBean>(ExampleReferenceBean, referenceQueue);

    assertNull(phantomReference.get());
  }

}
