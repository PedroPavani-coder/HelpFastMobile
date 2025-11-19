package com.example.helpfastmobile.data.network;

import com.example.helpfastmobile.data.model.AbrirChamadoDto;
import com.example.helpfastmobile.data.model.Chamado;
import com.example.helpfastmobile.data.model.Chat;
import com.example.helpfastmobile.data.model.ChatApiResponse;
import com.example.helpfastmobile.data.model.ChatIaResult;
import com.example.helpfastmobile.data.model.CreateChatDto;
import com.example.helpfastmobile.data.model.CreateUsuarioDbo;
import com.example.helpfastmobile.data.model.DocumentAssistantResponse;
import com.example.helpfastmobile.data.model.DocumentQuestionRequest;
import com.example.helpfastmobile.data.model.Faq;
import com.example.helpfastmobile.data.model.LoginDbo;
import com.example.helpfastmobile.data.model.N8nPayload;
import com.example.helpfastmobile.data.model.RegisterDbo;
import com.example.helpfastmobile.data.model.Status;
import com.example.helpfastmobile.data.model.UpdateStatusDto;
import com.example.helpfastmobile.data.model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {

    @GET("api/Cargos/usuarios")
    Call<List<User>> getCargosUsuarios();

    @GET("api/Cargos/usuarios/{usuarioId}")
    Call<User> getCargoUsuario(@Path("usuarioId") int usuarioId);

    @GET("api/Chamados")
    Call<List<Chamado>> getTodosChamados();

    @POST("api/Chamados/abrir")
    Call<Chamado> abrirChamado(@Body AbrirChamadoDto abrirChamadoDto);

    @GET("api/Chamados/{id}")
    Call<Chamado> getChamadoDetails(@Path("id") int chamadoId);

    @GET("api/Chamados/meus/{clienteId}")
    Call<List<Chamado>> getMeusChamados(@Path("clienteId") int clienteId);

    @GET("api/Chamados/status/{id}")
    Call<Status> getStatusChamado(@Path("id") int id);

    @PUT("api/Chamados/{id}/status")
    Call<Void> updateStatusChamado(@Path("id") int chamadoId, @Body UpdateStatusDto updateStatusDto);

    @GET("api/Chat/all")
    Call<ChatApiResponse> getChat(); // CORREÇÃO: Espera o objeto de resposta completo

    @POST("api/Chat")
    Call<Chat> createChat(@Body CreateChatDto createChatDto);

    @GET("api/Chat/{id}")
    Call<Chat> getChatById(@Path("id") int id);

    @GET("api/ChatIaResults")
    Call<List<ChatIaResult>> getChatIaResults();

    @POST("api/ChatIaResults")
    Call<ChatIaResult> createChatIaResult(@Body ChatIaResult chatIaResult);

    @GET("api/ChatIaResults/{id}")
    Call<ChatIaResult> getChatIaResult(@Path("id") int id);

    @POST("api/DocumentAssistant/perguntar")
    Call<DocumentAssistantResponse> perguntarDocumentAssistant(@Body DocumentQuestionRequest documentQuestionRequest);

    @GET("api/Faqs")
    Call<List<Faq>> getFaqs();

    @POST("api/Login")
    Call<User> login(@Body LoginDbo loginDbo);

    @POST("api/Login/register")
    Call<User> register(@Body RegisterDbo registerDbo);

    @GET("api/Usuarios")
    Call<List<User>> getUsuarios();

    @POST("api/Usuarios")
    Call<User> createUsuario(@Body CreateUsuarioDbo createUsuarioDbo);

    @GET("api/Usuarios/{id}")
    Call<User> getUsuario(@Path("id") int id);

    @DELETE("api/Usuarios/{id}")
    Call<Void> deleteUsuario(@Path("id") int usuarioId);

    @POST
    Call<Void> sendToN8n(@Url String url, @Body N8nPayload payload);

    @Streaming
    @GET("api/Relatorios/download")
    Call<ResponseBody> downloadRelatorio(@Query("mes") int mes, @Query("ano") int ano);
}
