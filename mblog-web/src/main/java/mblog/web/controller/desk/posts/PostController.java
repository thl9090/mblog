/**
 * 
 */
package mblog.web.controller.desk.posts;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import mblog.data.Group;
import mblog.data.Post;
import mblog.extend.event.LogEvent;
import mblog.extend.planet.PostPlanet;
import mblog.lang.EnumLog;
import mblog.persist.service.GroupService;
import mblog.web.controller.BaseController;
import mblog.web.controller.desk.Views;
import mtons.modules.pojos.Data;
import mtons.modules.pojos.UserProfile;

/**
 * @author langhsu
 *
 */
@Controller
@RequestMapping("/post")
public class PostController extends BaseController {
	@Autowired
	private PostPlanet postPlanet;
	@Autowired
	private GroupService groupService;
	@Autowired
	private ApplicationContext applicationContext;
	
	@RequestMapping(value = "/new/{groupKey}", method = RequestMethod.GET)
	public String view(@PathVariable String groupKey, ModelMap model) {
		Group group = groupService.getByKey(groupKey);
		model.put("group", group);
		return routeView(Views.ROUTE_POST_PUBLISH, group.getTemplate());
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String post(Post blog) {
		
		if (blog != null && StringUtils.isNotBlank(blog.getTitle())) {
			UserProfile up = getSubject().getProfile();
			
			handleAlbums(blog.getAlbums());
			blog.setAuthorId(up.getId());
			
			postPlanet.post(blog);
		}
		return "redirect:/home";
	}
	
	@RequestMapping("/delete/{id}")
	public @ResponseBody Data delete(@PathVariable Long id) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			UserProfile up = getSubject().getProfile();
			try {
				postPlanet.delete(id, up.getId());
				data = Data.success("操作成功", Data.NOOP);
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}
	
	@RequestMapping("/favor")
	public @ResponseBody Data favor(Long id, HttpServletRequest request) {
		Data data = Data.failure("操作失败");
		if (id != null) {
			try {
				UserProfile up = getSubject().getProfile();
				
				LogEvent evt = new LogEvent("source");
				
				if (up != null) {
					evt.setUserId(up.getId());
				}
				evt.setTargetId(id);
				evt.setType(EnumLog.FAVORED);
				evt.setIp(getIpAddr(request));
				applicationContext.publishEvent(evt);
				
				data = Data.success("操作成功,感谢您的支持!", Data.NOOP);
			} catch (Exception e) {
				data = Data.failure(e.getMessage());
			}
		}
		return data;
	}
	
}
