List call(Map args) {
    args = args ?: [:]

    final classNames = args.classNames ?: findFiles(glob: "**/tests/*Tests.groovy").collect { it.name - ".groovy" }
    final libraryName = args.library
    final namespace = args.namespace
    final setter = args.setter

    final nsObject = library(identifier: libraryName, changelog: false)."$namespace"

    return classNames.collect {
        final instance = nsObject."$it".new()

        if (setter) {
            setter.call(instance)
        }

        return instance
    }
}