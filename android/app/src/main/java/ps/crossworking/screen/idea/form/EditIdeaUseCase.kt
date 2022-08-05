package ps.crossworking.screen.idea.form

import ps.crossworking.exceptions.IdeaSmallDescriptionTooLongException
import ps.crossworking.exceptions.IdeaTitleTooLongException
import ps.crossworking.model.Idea
import ps.crossworking.repository.IIdeaRepository
import javax.inject.Inject

interface IEditIdeaUseCase {
    suspend operator fun invoke(
        ideaId: String,
        title: String,
        smallDescription: String,
        description: String
    ): Idea
}

class EditIdeaUseCase @Inject constructor(
    val repository: IIdeaRepository
) : IEditIdeaUseCase {

    private val titleMaxSize = 25
    private val shortDescriptionMaxSize = 50

    override suspend fun invoke(
        ideaId: String,
        title: String,
        smallDescription: String,
        description: String
    ): Idea {
        if (title.length > titleMaxSize)
            throw IdeaTitleTooLongException()
        if (smallDescription.length > shortDescriptionMaxSize)
            throw IdeaSmallDescriptionTooLongException()
        return repository.editIdea(ideaId, title, smallDescription, description)
    }
}
