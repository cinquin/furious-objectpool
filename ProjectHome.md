# Fast and Light Java object pool. #

This pool implementation is based on java.util.concurrent package. The class
ConcurrentLinkedQueue are used when the pool grow automaticaly. LinkedBlockingQueue are used when the pool is empty, in this case, the pool will wait for an available object.


```

// Create your PoolSettings with an instance of PoolableObject
PoolSettings<StringBuilder> poolSettings = new PoolSettings<StringBuilder>(
		new PoolableObjectBase<StringBuilder>() {
			@Override
			public StringBuilder make() {
				return new StringBuilder();
			}
			@Override
			public void activate(StringBuilder t) {
				t.setLength(0);
			}
		});
// Add some settings
poolSettings.min(0).max(10);

// Get the objectPool instance using a Singleton Design Pattern is a
// good idea
ObjectPool<StringBuilder> objectPool = poolSettings.pool();

// Use your pool
StringBuilder buffer = null;
try {
	buffer = objectPool.getObj();
	// Do something with your object
	buffer.append("yyyy");
} catch (PoolException e) {
	e.printStackTrace();
} finally {
	// Don't forget to return object in the pool
	objectPool.returnObj(buffer);
}
```


## Benchmark comparison between "furious-pool" and and "Apache-commons" ##

x-axis : concurrent threads<br />
y-axis : time in milliseconds

![http://courirasteluce.lescigales.org/bench_furious_pool_vs_apache.png](http://courirasteluce.lescigales.org/bench_furious_pool_vs_apache.png)
Lower is better

## Maven integration ##
```

<dependency>
  <groupId>nf.fr.eraasoft</groupId>
  <artifactId>objectpool</artifactId>
  <version>1.1.2</version>
</dependency>

<repositories>
  <repository>
     <id>oss.sonatype</id>
     <name>Sonatype repository</name>
     <url>http://oss.sonatype.org/content/groups/public</url>
  </repository>
</repositories>

```