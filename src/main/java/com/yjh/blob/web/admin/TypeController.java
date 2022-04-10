package com.yjh.blob.web.admin;

import com.yjh.blob.NotFoundException;
import com.yjh.blob.Service.TypeService;
import com.yjh.blob.po.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.model.IModel;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Resource
    private TypeService typeService;


    @GetMapping("/types")
    public String types(@PageableDefault(size = 3,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    @GetMapping("/types/{id}/input")
    public String edit(Model model, @PathVariable Long id){
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        Type type1 = typeService.getTypeName(type.getName());
        if(type1!=null){
            bindingResult.rejectValue("name","nameError","不能添加重复分类");
        }
        if(bindingResult.hasErrors()){
            return "admin/types-input";
        }
        Type t = typeService.saveType(type);
        if(t==null){
            redirectAttributes.addFlashAttribute("message","新增失败");
        }
        else {
            redirectAttributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/types";
    }

    @PostMapping("/types/{id}")
    public String editpost(@Valid Type type, BindingResult bindingResult,@PathVariable Long id ,RedirectAttributes redirectAttributes){
        Type type1 = typeService.getTypeName(type.getName());
        if(type1!=null){
            bindingResult.rejectValue("name","nameError","不能添加重复分类");
        }
        if(bindingResult.hasErrors()){
            return "admin/types-input";
        }
        Type t = typeService.updateType(id,type);
        if(t==null){
            redirectAttributes.addFlashAttribute("message","更新失败");
        }
        else {
            redirectAttributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes){
        typeService.deleteType(id);
        redirectAttributes.addFlashAttribute("message","更新成功");
        return "redirect:/admin/types";
    }
}
