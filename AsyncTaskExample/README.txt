AsyncTask

总结起来就是: 3个泛型，4个步骤。



3个泛型指的是什么呢？我们来看看AsyncTask这个抽象类的定义，当我们定义一个类来继承AsyncTask这个类的时候，我们需要为其指定3个泛型参数：

AsyncTask　<Params, Progress, Result>
•Params: 这个泛型指定的是我们传递给异步任务执行时的参数的类型
•Progress: 这个泛型指定的是我们的异步任务在执行的时候将执行的进度返回给UI线程的参数的类型
•Result: 这个泛型指定的异步任务执行完后返回给UI线程的结果的类型

我们在定义一个类继承AsyncTask类的时候，必须要指定好这三个泛型的类型，如果都不指定的话，则都将其写成Void，例如：

AsyncTask <Void, Void, Void>



4个步骤：当我们执行一个异步任务的时候，其需要按照下面的4个步骤分别执行

•onPreExecute(): 这个方法是在执行异步任务之前的时候执行，并且是在UI Thread当中执行的，通常我们在这个方法里做一些UI控件的初始化的操作，例如弹出要给ProgressDialog
•doInBackground(Params... params): 在onPreExecute()方法执行完之后，会马上执行这个方法，这个方法就是来处理异步任务的方法，
 Android操作系统会在后台的线程池当中开启一个worker thread来执行我们的这个方法，所以这个方法是在worker thread当中执行的，
 这个方法执行完之后就可以将我们的执行结果发送给我们的最后一个 onPostExecute 方法，在这个方法里，我们可以从网络当中获取数据等一些耗时的操作
•onProgressUpdate(Progess... values): 这个方法也是在UI Thread当中执行的，我们在异步任务执行的时候，有时候需要将执行的进度返回给我们的UI界面，
 例如下载一张网络图片，我们需要时刻显示其下载的进度，就可以使用这个方法来更新我们的进度。这个方法在调用之前，
 我们需要在 doInBackground 方法中调用一个 publishProgress(Progress) 的方法来将我们的进度时时刻刻传递给 onProgressUpdate 方法来更新
•onPostExecute(Result... result): 当我们的异步任务执行完之后，就会将结果返回给这个方法，这个方法也是在UI Thread当中调用的，我们可以将返回的结果显示在UI控件上

为什么我们的AsyncTask抽象类只有一个 doInBackground 的抽象方法呢？？原因是，我们如果要做一个异步任务，我们必须要为其开辟一个新的Thread，
让其完成一些操作，而在完成这个异步任务时，我可能并不需要弹出要给ProgressDialog，我并不需要随时更新我的ProgressDialog的进度条，
我也并不需要将结果更新给我们的UI界面，所以除了 doInBackground 方法之外的三个方法，都不是必须有的，因此我们必须要实现的方法是 doInBackground 方法。



























