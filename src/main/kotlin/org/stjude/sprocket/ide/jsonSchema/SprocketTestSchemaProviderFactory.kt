package org.stjude.sprocket.ide.jsonSchema

import com.intellij.openapi.project.Project
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory


class SprocketTestSchemaProviderFactory : JsonSchemaProviderFactory {
    override fun getProviders(project: Project): List<JsonSchemaFileProvider?> {
        return listOf(SprocketTestSchemaProvider(project))
    }
}