List call(Map args) {
    args = args ?: [:]

    final libraryName = args.library
    final namespace = args.namespace

    assert args.library
    assert args.namespace

    final setter = args.setter

    final classNames = args.classNames ?: getClassNamesByGlob(namespace, args.glob)
    final nsObject = library(identifier: libraryName, changelog: false)."$namespace"

    return classNames.collect {
        final instance = nsObject."$it".new()

        if (setter) {
            setter.call(instance)
        }

        return instance
    }
}

private List getClassNamesByGlob(String namespace, String glob) {
    if (!Jenkins.instance.pluginManager.getPlugin("pipeline-utility-steps")) {
        throw new Exception("hydrate.glob requires the Pipeline Utility Steps Plugin")
    }

    if (!glob) {
        glob = "src/" + namespace.replace(".", "/") + "/*.groovy"
    }

    return findFiles(glob: glob).collect { it.name - ".groovy" }
}