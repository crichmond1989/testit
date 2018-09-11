List call(Map args) {
    args = args ?: [:]

    final libraryName = args.library
    final namespace = args.namespace

    assert args.library
    assert args.namespace
    assert args.classNames || args.glob

    final setter = args.setter

    final classNames = args.classNames ?: getClassNamesByGlob(args.glob)
    final nsObject = library(identifier: libraryName, changelog: false)."$namespace"

    return classNames.collect {
        final instance = nsObject."$it".new()

        if (setter) {
            setter.call(instance)
        }

        return instance
    }
}

private List getClassNamesByGlob(String glob) {
    if (!Jenkins.instance.pluginManager.getPlugin("Pipeline+Utility+Steps+Plugin")) {
        throw new Exception("hydrate.glob requires the Pipeline Utility Steps Plugin")
    }

    return findFiles(glob: glob).collect { it.name - ".groovy" }
}