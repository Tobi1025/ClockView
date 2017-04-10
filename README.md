#### **之前由于写的自定义view太过仓促，有很多没有注意的地方，需要优化，在以后的博客中会慢慢改进~此篇文章中提出以下几点，希望大家一起进步**
* 获取自定义属性值后，需要调用typedArray.recycle()将TypedArray释放
* `避免在onDraw方法里面执行对象的创建`，因为他会迅速增加内存的使用，而且很容易引起频繁的gc，甚至是内存抖动。
* 当通过canvas.rotate()等操作对canvas进行变换时，要注意canvas.save()和canvas.restore()应该成对出现，不然会报错;

### 附上效果图：
![](https://ww2.sinaimg.cn/large/006tNc79ly1feeawvecymg30f00qo7wh.gif)
