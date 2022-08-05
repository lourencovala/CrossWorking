package ps.crossworking.di

import android.content.ContentResolver
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ps.crossworking.common.authToken
import ps.crossworking.helper.*
import ps.crossworking.repository.*
import ps.crossworking.screen.idea.GetIdeaUseCase
import ps.crossworking.screen.idea.IGetIdeaUseCase
import ps.crossworking.screen.idea.candidatures.GetIdeaCandidaturesUseCase
import ps.crossworking.screen.idea.candidatures.IGetIdeaCandidaturesUseCase
import ps.crossworking.screen.idea.details.*
import ps.crossworking.screen.idea.feed.GetFeedUseCase
import ps.crossworking.screen.idea.feed.IGetFeedUseCase
import ps.crossworking.screen.idea.form.CreateIdeaUseCase
import ps.crossworking.screen.idea.form.EditIdeaUseCase
import ps.crossworking.screen.idea.form.ICreateIdeaUseCase
import ps.crossworking.screen.idea.form.IEditIdeaUseCase
import ps.crossworking.screen.idea.userlist.GetUserIdeasUseCase
import ps.crossworking.screen.idea.userlist.IGetUserIdeasUseCase
import ps.crossworking.screen.skill.GetCategoryUseCase
import ps.crossworking.screen.skill.IGetCategoryUseCase
import ps.crossworking.screen.skill.addIdeaSkill.AddIdeaSkillUseCase
import ps.crossworking.screen.skill.addIdeaSkill.IAddIdeaSkillUseCase
import ps.crossworking.screen.skill.addUserSkill.AddUserSkillUseCase
import ps.crossworking.screen.skill.addUserSkill.IAddUserSkillUseCase
import ps.crossworking.screen.skill.manageIdea.DeleteIdeaSkillUseCase
import ps.crossworking.screen.skill.manageIdea.GetIdeaSkillsUseCase
import ps.crossworking.screen.skill.manageIdea.IDeleteIdeaSkillUseCase
import ps.crossworking.screen.skill.manageIdea.IGetIdeaSkillsUseCase
import ps.crossworking.screen.skill.manageUser.DeleteUserSkillUseCase
import ps.crossworking.screen.skill.manageUser.GetUserSkillsUseCase
import ps.crossworking.screen.skill.manageUser.IDeleteUserSkillUseCase
import ps.crossworking.screen.skill.manageUser.IGetUserSkillsUseCase
import ps.crossworking.screen.user.GetUserUseCase
import ps.crossworking.screen.user.IGetUserUseCase
import ps.crossworking.screen.user.IIsLoggedInUseCase
import ps.crossworking.screen.user.IsLoggedInUseCase
import ps.crossworking.screen.user.authentication.ISignUpGoogleUseCase
import ps.crossworking.screen.user.authentication.SignUpGoogleUseCase
import ps.crossworking.screen.user.authentication.signin.ISignInUseCase
import ps.crossworking.screen.user.authentication.signin.SignInUseCase
import ps.crossworking.screen.user.authentication.signup.ISignUpUseCase
import ps.crossworking.screen.user.authentication.signup.SignUpUseCase
import ps.crossworking.screen.user.form.AddNameAndPicUriUseCase
import ps.crossworking.screen.user.form.IAddNameAndPicUriUseCase
import ps.crossworking.screen.user.form.IUploadImageAwsUseCase
import ps.crossworking.screen.user.form.UploadImageAwsUseCase
import ps.crossworking.screen.user.profile.own.ISignOutUseCase
import ps.crossworking.screen.user.profile.own.SignOutUseCase
import ps.crossworking.screen.user.results.GetResultsUseCase
import ps.crossworking.screen.user.results.IGetResultsUseCase
import ps.crossworking.repository.IUserRepository
import ps.crossworking.repository.UserRepository
import ps.crossworking.service.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    @Singleton
    fun provideViewModel(
        getUser: IGetOwnUserUseCase,
        isLogged: IIsLoggedInUseCase,
        signOutUseCase: ISignOutUseCase
    ): HelperViewModel {
        return HelperViewModel(getUser, isLogged, signOutUseCase)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(authToken)
            .baseUrl("https://crossworking.link/api/")
            //.baseUrl("http://10.0.2.2:8080/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGoogleSignIn(@ApplicationContext context: Context): GoogleSignInClient {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()

        return GoogleSignIn.getClient(context, gso)
    }

    @Provides
    @Singleton
    fun providesIdeaService(retrofit: Retrofit): IdeaService {
        return retrofit.create(IdeaService::class.java)
    }

    @Provides
    @Singleton
    fun providesUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideSkillService(retrofit: Retrofit): SkillService {
        return retrofit.create(SkillService::class.java)
    }

    @Provides
    @Singleton
    fun provideCategoryService(retrofit: Retrofit): CategoryService {
        return retrofit.create(CategoryService::class.java)
    }

    @Provides
    @Singleton
    fun provideCandidatureService(retrofit: Retrofit): CandidatureService {
        return retrofit.create(CandidatureService::class.java)
    }

    @Provides
    @Singleton
    fun provideInputStream(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface AuthModuleInterface {
        @Binds
        @Singleton
        fun provideSignUpUseCase(useCase: SignUpUseCase): ISignUpUseCase

        @Binds
        @Singleton
        fun providesAuthService(firebase: AuthService): IAuthService

        @Binds
        @Singleton
        fun provideSignUpGoogleUseCase(userCase: SignUpGoogleUseCase): ISignUpGoogleUseCase

        @Binds
        @Singleton
        fun provideAddNameAndPicUseCase(userCase: AddNameAndPicUriUseCase): IAddNameAndPicUriUseCase

        @Binds
        @Singleton
        fun provideUploadImageAwsUseCase(useCase: UploadImageAwsUseCase): IUploadImageAwsUseCase

        @Binds
        @Singleton
        fun providesSignInUseCase(useCase: SignInUseCase): ISignInUseCase

        @Binds
        @Singleton
        fun provideGetUserUseCase(userCase: GetUserUseCase): IGetUserUseCase

        @Binds
        @Singleton
        fun provideSignOutUseCase(useCase: SignOutUseCase): ISignOutUseCase

        @Binds
        @Singleton
        fun provideIsLoggedInUseCase(useCase: IsLoggedInUseCase): IIsLoggedInUseCase


        @Binds
        @Singleton
        fun provideGetOwnUserUseCase(useCase: GetOwnUserUseCase): IGetOwnUserUseCase

        @Binds
        @Singleton
        fun provideAuthRepository(repository: AuthRepository): IAuthRepository

        @Binds
        @Singleton
        fun provideUserRepository(repository: UserRepository): IUserRepository

        @Binds
        @Singleton
        fun provideS3Service(s3Service: S3Service): IImageStorage
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInterface {

        @Binds
        @Singleton
        fun provideIdeaRepo(repository: IdeaRepository): IIdeaRepository

        @Binds
        @Singleton
        fun provideCandidatureRepo(repository: CandidatureRepository): ICandidatureRepository

        @Binds
        @Singleton
        fun provideUseCase(useCase: GetFeedUseCase): IGetFeedUseCase

        @Binds
        @Singleton
        fun provideGetIdeaDetailsUseCase(useCase: GetIdeaUseCase): IGetIdeaUseCase

        @Binds
        @Singleton
        fun provideCreateIdeaUseCase(useCase: CreateIdeaUseCase): ICreateIdeaUseCase

        @Binds
        @Singleton
        fun provideEditIdeaUseCase(useCase: EditIdeaUseCase): IEditIdeaUseCase

        @Binds
        @Singleton
        fun provideDeleteIdeaUseCase(useCase: DeleteIdeaUseCase): IDeleteIdeaUseCase

        @Binds
        @Singleton
        fun provideApplyToIdeaUseCase(useCase: ApplyToIdeaUseCase): IApplyToIdeaUseCase

        @Binds
        @Singleton
        fun provideGetUserIdeasUseCase(useCase: GetUserIdeasUseCase): IGetUserIdeasUseCase

        @Binds
        @Singleton
        fun provideAddSkillUseCase(useCase: AddUserSkillUseCase): IAddUserSkillUseCase

        @Binds
        @Singleton
        fun provideDeleteUserSkillUseCase(useCase: DeleteUserSkillUseCase): IDeleteUserSkillUseCase

        @Binds
        @Singleton
        fun provideGetUserSkillsUseCase(useCase: GetUserSkillsUseCase): IGetUserSkillsUseCase

        @Binds
        @Singleton
        fun provideSkillRepository(repository: SkillRepository): ISkillRepository

        @Binds
        @Singleton
        fun provideGetCategoryUseCase(useCase: GetCategoryUseCase): IGetCategoryUseCase

        @Binds
        @Singleton
        fun provideGetIdeaSkillUseCase(useCase: GetIdeaSkillsUseCase): IGetIdeaSkillsUseCase


        @Binds
        @Singleton
        fun provideAddIdeaSkillUseCase(useCase: AddIdeaSkillUseCase): IAddIdeaSkillUseCase

        @Binds
        @Singleton
        fun provideDeleteIdeaSkillUseCase(useCase: DeleteIdeaSkillUseCase): IDeleteIdeaSkillUseCase

        @Binds
        @Singleton
        fun provideGetCandidateUseCase(useCase: GetCandidatureStatusUseCase): IGetCandidatureStatusUseCase

        @Binds
        @Singleton
        fun provideUndoApplyIdeaUseCase(useCase: UndoApplyIdeaUseCase): IUndoApplyIdeaUseCase

        @Binds
        @Singleton
        fun provideGetResultsUseCase(useCase: GetResultsUseCase): IGetResultsUseCase

        @Binds
        @Singleton
        fun provideGetIdeaCandidatesUseCase(useCase: GetIdeaCandidaturesUseCase): IGetIdeaCandidaturesUseCase

    }
}