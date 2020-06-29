package zust.xyt.controller;

import org.bouncycastle.math.raw.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import zust.xyt.ResponseResult;
import zust.xyt.entity.User;
import zust.xyt.entity.Video;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author AndrewElvis
 * @date 2020-06-27-18:48
 * @description
 */
@Controller
@RequestMapping("/views/video")
public class VideoController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/{id}")
    public String singleVideo(@PathVariable String id, Model model) {
        ResponseResult res = restTemplate.getForObject("http://SERVICE-VIDEO/vod/getPlayUrl/" + id,
                ResponseResult.class);
        Video video = restTemplate.getForObject("http://SERVICE-VIDEO/video/getBySourceId/" + id, Video.class);
        List<LinkedHashMap> videoList = restTemplate.getForObject("http://SERVICE-VIDEO/video", List.class);
        for (LinkedHashMap v : videoList) {
            System.out.println(v.get("userId"));
            User user = restTemplate.getForObject("http://SERVICE-USER/user/" + v.get("userId"), User.class);
            v.put("userNickName", user.getNickname());
            v.put("userAvatar", user.getAvatar());
        }
        User me = restTemplate.getForObject("http://SERVICE-USER/user/" + "1273857412729692122", User.class);
        model.addAttribute("user",me);
        model.addAttribute("url", res.getData().get("url"));
        model.addAttribute("video", video);
        model.addAttribute("videoList", videoList);
        return "single-video";
    }

    @GetMapping("/toChannals/{id}")
    public String channals(@PathVariable String id,Model model){
        ArrayList topChannals = restTemplate.getForObject("http://SERVICE-USER/user/topChannals", ArrayList.class);
        //bar
        ArrayList users = restTemplate.getForObject("http://SERVICE-USER/user/subscribe/" + id, ArrayList.class);
        User me = restTemplate.getForObject("http://SERVICE-USER/user/" + id, User.class);
        model.addAttribute("user",me);
        model.addAttribute("subscribes",users);
        model.addAttribute("topChannals",topChannals);
        return "browse-channals";
    }


    @GetMapping("/toHome/{id}")
    public String home(@PathVariable String id, Model model) {
        List<LinkedHashMap> result = restTemplate.getForObject("http://SERVICE-VIDEO/video", List.class);
        //bar
        ArrayList users = restTemplate.getForObject("http://SERVICE-USER/user/subscribe/" + id, ArrayList.class);
        ArrayList findChannals = restTemplate.getForObject("http://SERVICE-USER/user/findChannals/" + id, ArrayList.class);
        //bar
        User me = restTemplate.getForObject("http://SERVICE-USER/user/" + id, User.class);
        model.addAttribute("user",me);
        model.addAttribute("findChannals",findChannals);
        model.addAttribute("subscribes",users);
        for (LinkedHashMap video : result) {
            System.out.println(video.get("userId"));
            User user = restTemplate.getForObject("http://SERVICE-USER/user/" + video.get("userId"), User.class);
            video.put("userNickName", user.getNickname());
            video.put("userAvatar", user.getAvatar());
        }
        model.addAttribute("videoList", result);
        return "home";
    }

    @GetMapping("/toChannel/{id}")
    public String channel(@PathVariable String id, Model model) {

        return "single-channal";
    }
}
