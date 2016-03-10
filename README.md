RxDataLoading
-------------
A simple example of loading data from multiple sources using Rx.

#1. Common

To load data used multiple sources. Typically, a remote web server and local database. The data from the web server only update the local database, and never used for UI.


##1.1  Requirements to data sources


All sources must return [`Observable <T>`](https://github.com/ReactiveX/RxJava/wiki/Observable).


Remote data source (the remote) after emits call the subscriber method `.onComplete ()`.

Local data source (the local) emits data is infinite and never call .onComplete () method. Thus, the local is always able to notify subscribers about the changes in the database.

To the data automatically updates to the UI to update the local database is necessary that the subscriber subscribed to the local Observable, a not the remote.


##1.2 Data sources features 

The local emits data with a delay of 100 ms, the remote - 2000 ms.

If need to load by combining sources not recommended using `.concat()` method.

**Example 1**

```java
BehaviorSubject local = BehaviorSubject.create(1);
Observable remote = Observable.just(2);
Observable result = Observable.concat(local, remote);
```
```
Output:
1
```
Since the local is infinitely emits data and does't call `.onComplete()` then the remote never not start emit data.

**Example 2**

```java
BehaviorSubject local = BehaviorSubject.create (1);
Observable remote = Observable.just (2);
Observable result = Observable.concat (remote, local);
```

```
Output:
1
2
```

##1.3 Simulation data sources


The `Local` class can wrap data to `Observable` and add to model `PropertyChangeListener` for observe property changes.

The `Remote` just emit data with passed identificator.

#2. Loading patterns

The remote `.getData()` method automatically cached data in `.doOnNext()`.

##2.1 Pattern 1

###2.1.1 Description

• need to loading `T` or `List <T>`
• new data is always replace old data


###2.1.2 Example

Detailed information about the entity, list without pagination.

###2.1.3 Implementation


```java
Observable<Model> localData = local.getDataById(id);
Observable<Model> remoteData = remote.getDataById(id).ignoreElements();

Observable<Model> result = Observable.merge(localData, remoteData);
```

##2.2 Pattern 2


###2.2.1 Description

• need to loading `List <T>`
• pagination


###2.2.2 Example

List with pagination.

###2.2.3 Implementation


```java
Observable<Integer> offsetObservable = Observable.just(0, 1, 2, 3, 4);
Observable<List<Model>> localData = local.getAllData().take(1);

Observable<List<Model>> remoteData = offsetObservable
                .concatMap(new Func1<Integer, Observable<List<Model>>>() {
                    @Override
                    public Observable<List<Model>> call(final Integer offset) {
                        return remote.getDataByOffset(offset);
                    }
                })
                .map(new Func1<List<Model>, List<Integer>>() {
                    @Override
                    public List<Integer> call(List<Model> models) {
                        List<Integer> ids = new ArrayList<Integer>();
                        for (Model model : models) {
                            ids.add(model.getId());
                        }
                        return ids;
                    }
                })
                .flatMap(new Func1<List<Integer>, Observable<List<Model>>>() {
                    @Override
                    public Observable<List<Model>> call(List<Integer> ids) {
                        return local.getDataById(ids);
                    }
                });

Observable<List<Model>> result = Observable.concat(localData, remoteData);
```

#3. Links


1. [Subscribe It While It's Hot: Cached Rest Requests With RxJava](http://fedepaol.github.io/blog/2016/01/01/cached-rest-requests-with-rxjava/ ) 
The article describes the approach of caching and load data, which is represented in the repository.

2. [Chaining multiple sources with RxJava](https://medium.com/@murki/chaining-multiple-sources-with-rxjava-20eb6850e5d9#.4tzzbn8qm)
About how to load data from multiple sources. Unlike the approach to consumer data comes from different sources.
