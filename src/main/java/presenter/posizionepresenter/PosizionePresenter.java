package presenter.posizionepresenter;

import model.posizionemanagement.Posizione;
import model.posizionemanagement.PosizioneDAO;
import presenter.http.presenter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet(name="posizionepresenter", value = "/PosizionePresenter/*")
public class PosizionePresenter extends presenter {

}
