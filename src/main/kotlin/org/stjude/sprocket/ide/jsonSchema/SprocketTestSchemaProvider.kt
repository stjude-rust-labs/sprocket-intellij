package org.stjude.sprocket.ide.jsonSchema

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.TestSourcesFilter
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.SchemaType
import org.jetbrains.annotations.NonNls
import com.jetbrains.jsonSchema.remote.JsonFileResolver


class SprocketTestSchemaProvider(val project: Project) : JsonSchemaFileProvider {
    override fun isAvailable(file: VirtualFile): Boolean {
        return TestSourcesFilter.isTestSources(file, project)
    }

    override fun getSchemaFile(): VirtualFile? {
        return JsonFileResolver.urlToFile(SCHEMA_URL)
    }

    override fun getSchemaType(): SchemaType {
        return SchemaType.remoteSchema
    }

    override fun getName(): String {
        return "Sprocket test"
    }

    override fun getRemoteSource(): @NonNls String {
        return SCHEMA_URL
    }

    private companion object {
        const val SCHEMA_URL = "https://raw.githubusercontent.com/stjude-rust-labs/sprocket/refs/heads/main/jsonschemas/sprocket-test.json"
    }
}