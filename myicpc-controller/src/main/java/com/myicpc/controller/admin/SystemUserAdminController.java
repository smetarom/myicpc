package com.myicpc.controller.admin;

import com.myicpc.commons.utils.MessageUtils;
import com.myicpc.controller.GeneralAdminController;
import com.myicpc.model.security.SystemUser;
import com.myicpc.model.security.SystemUserRole;
import com.myicpc.repository.security.SystemUserRepository;
import com.myicpc.enums.UserRoleEnum;
import com.myicpc.service.exception.ReportException;
import com.myicpc.service.report.SystemUserReportService;
import com.myicpc.service.user.SystemUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller handles system user administration views
 * 
 * @author Roman Smetana
 */
@Controller
@Transactional
@SessionAttributes({ "systemUser" })
public class SystemUserAdminController extends GeneralAdminController {

	@Autowired
	private SystemUserService systemUserService;

	@Autowired
	private SystemUserReportService systemUserReport;

	@Autowired
	private SystemUserRepository systemUserRepository;

	/**
	 * Shows users administration page
	 * 
	 * @param model
	 * @return view
	 */
	@RequestMapping(value = "/private/users", method = RequestMethod.GET)
	public String users(Model model) {

		model.addAttribute("users", systemUserRepository.findAllWithRolesOrderByLastname());
		return "private/users/userList";
	}

	/**
	 * Shows a create new user page
	 * 
	 * @param model
	 * @return view
	 */
	@RequestMapping(value = "/private/users/create", method = RequestMethod.GET)
	@Transactional
	public String createUser(Model model) {
		SystemUser user = new SystemUser();
		user.setEnabled(true);
		model.addAttribute("systemUser", user);
		model.addAttribute("roles", getAllRoles());
		model.addAttribute("mode", "create");
		model.addAttribute("headlineTitle", getMessage("userAdmin.create"));
		model.addAttribute("formURL", "/private/users/create");
		model.addAttribute("showCredentials", true);
		return "private/users/editUser";
	}

	/**
	 * Shows an edit existing user page
	 * 
	 * @param userId
	 *            system user ID
	 * @param model
	 * @param redirectAttributes
	 * @return view
	 */
	@RequestMapping(value = "/private/users/{userId}/edit", method = RequestMethod.GET)
	@Transactional
	public String editUser(@PathVariable Long userId, Model model, RedirectAttributes redirectAttributes) {
		SystemUser user = systemUserRepository.findById(userId);
		if (user == null) {
			errorMessage(redirectAttributes, "userAdmin.noResult");
			return "redirect:/private/users";
		}

		List<String> stringRoles = new ArrayList<String>();
		for (SystemUserRole role : user.getRoles()) {
			stringRoles.add(role.getAuthority());
		}
		user.setStringRoles(stringRoles);

		model.addAttribute("systemUser", user);
		model.addAttribute("roles", getAllRoles());
		model.addAttribute("mode", "edit");
		model.addAttribute("headlineTitle", getMessage("userAdmin.edit"));
		model.addAttribute("formURL", "/private/users/updateUser");
		return "private/users/editUser";
	}

	/**
	 * Shows change a password page
	 * 
	 * @param userId
	 *            system user ID
	 * @param model
	 * @param redirectAttributes
	 * @return view
	 */
	@RequestMapping(value = "/private/users/{userId}/changePassword", method = RequestMethod.GET)
	@Transactional
	public String changePassword(@PathVariable Long userId, Model model, RedirectAttributes redirectAttributes) {
		SystemUser user = systemUserRepository.findOne(userId);
		if (user == null) {
			errorMessage(redirectAttributes, "userAdmin.noResult");
			return "redirect:/private/users";
		}

		model.addAttribute("systemUser", user);
		model.addAttribute("requireOldPassword", false);
		model.addAttribute("breadcrumbURL", "/private/users");
		model.addAttribute("formURL", "/private/users/changePassword");

		return "private/users/changePassword";
	}

	/**
	 * Processes a user delete
	 * 
	 * @param userId
	 *            system user ID
	 * @param model
	 * @param redirectAttributes
	 * @return view
	 */
	@RequestMapping(value = "/private/users/{userId}/delete", method = RequestMethod.GET)
	@Transactional
	public String deleteUser(@PathVariable Long userId, Model model, RedirectAttributes redirectAttributes) {
		SystemUser user = systemUserRepository.findOne(userId);
		if (user == null) {
			errorMessage(redirectAttributes, "userAdmin.noResult");
			return "redirect:/private/users";
		}

		systemUserRepository.delete(user);
		successMessage(redirectAttributes, "userAdmin.delete.success", user.getUsername());

		return "redirect:/private/users";
	}

	/**
	 * Processes a change of user password
	 * 
	 * @param user
	 *            system user
	 * @param oldPassword
	 *            old password
	 * @param result
	 * @param model
	 * @return view
	 */
	@RequestMapping(value = "/private/users/changePassword", method = RequestMethod.POST)
	@Transactional
	public String changePassword(@Valid @ModelAttribute("systemUser") SystemUser user, @RequestParam(required = false) String oldPassword,
			BindingResult result, Model model) {
		if (hasErrorPasswordChange(result, model, user)) {
			model.addAttribute("requireOldPassword", false);
			model.addAttribute("breadcrumbURL", "/private/users");
			model.addAttribute("formURL", "/private/users/changePassword");
			return "private/users/changePassword";
		}

		user.setPassword(systemUserService.hashPassword(user.getPassword()));

		systemUserRepository.save(user);

		return "redirect:/private/users";
	}

	/**
	 * Processes a user creation
	 * 
	 * @param user
	 *            system user
	 * @param result
	 * @param model
	 * @return view
	 */
	@RequestMapping(value = "/private/users/create", method = RequestMethod.POST)
	@Transactional
	public String createUser(@Valid @ModelAttribute("systemUser") SystemUser user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("roles", getAllRoles());
		model.addAttribute("mode", "create");
		model.addAttribute("headlineTitle", getMessage("userAdmin.create"));
		model.addAttribute("formURL", "/private/users/create");
		model.addAttribute("showCredentials", true);
		if (hasErrorPasswordChange(result, model, user)) {
			return "private/users/editUser";
		}

		user.setPassword(systemUserService.hashPassword(user.getPassword()));

		user = systemUserService.mergeUser(user);
		successMessage(redirectAttributes, "userAdmin.create.success", user.getUsername());

		return "redirect:/private/users";
	}

	/**
	 * Processes a user update
	 * 
	 * @param user
	 *            system user
	 * @param result
	 * @param model
	 * @return view
	 */
	@RequestMapping(value = "/private/users/updateUser", method = RequestMethod.POST)
	@Transactional
	public String updateUser(@Valid @ModelAttribute("systemUser") SystemUser user, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("roles", getAllRoles());
		model.addAttribute("headlineTitle", getMessage("userAdmin.edit"));
		model.addAttribute("formURL", "/private/users/updateUser");
		if (result.hasErrors()) {
			return "private/users/editUser";
		}

		systemUserService.mergeUser(user);
		successMessage(redirectAttributes, "userAdmin.edit.success", user.getUsername());

		return "redirect:/private/users";
	}

	/**
	 * Shows an user profile
	 * 
	 * @param model
	 * @param redirectAttributes
	 * @return view
	 */
	@RequestMapping(value = "/private/profile", method = RequestMethod.GET)
	public String profile(Model model, RedirectAttributes redirectAttributes) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SystemUser user = systemUserRepository.findByUsername(auth.getName());

		if (user == null) {
			errorMessage(redirectAttributes, "userAdmin.noResult");
			return "redirect:/private/home";
		}

		model.addAttribute("systemUser", user);

		return "private/users/profile";
	}

	/**
	 * Shows change password by user page
	 * 
	 * @param model
	 * @param redirectAttributes
	 * @return view
	 */
	@RequestMapping(value = "/private/profile/changePassword", method = RequestMethod.GET)
	public String profileChangePassword(Model model, RedirectAttributes redirectAttributes) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SystemUser user = systemUserRepository.findByUsername(auth.getName());

		if (user == null) {
			errorMessage(redirectAttributes, "userAdmin.noResult");
			return "redirect:/private/users";
		}
		user.setOldPassword(user.getPassword());

		model.addAttribute("systemUser", user);
		model.addAttribute("requireOldPassword", true);
		model.addAttribute("breadcrumbURL", "/private/profile");
		model.addAttribute("formURL", "/private/profile/changePassword");

		return "private/users/changePassword";
	}

	/**
	 * Processes a change password by user
	 * 
	 * @param user
	 *            system user
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @return view
	 */
	@RequestMapping(value = "/private/profile/changePassword", method = RequestMethod.POST)
	@Transactional
	public String profileChangePassword(@Valid @ModelAttribute("systemUser") SystemUser user, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (hasErrorPasswordChange(result, model, user)) {
			model.addAttribute("requireOldPassword", true);
			model.addAttribute("breadcrumbURL", "/private/profile");
			model.addAttribute("formURL", "/private/profile/changePassword");
			return "private/users/changePassword";
		}

		user.setPassword(systemUserService.hashPassword(user.getPassword()));

		systemUserRepository.save(user);
		successMessage(redirectAttributes, "userAdmin.profile.oldPassword.success");

		return "redirect:/private/profile";
	}

	/**
	 * Processes an user import
	 * 
	 * @param usersCSV
	 *            users file
	 * @param model
	 * @param redirectAttributes
	 * @return view
	 * @throws java.io.IOException
	 * @throws java.text.ParseException
	 */
	@RequestMapping(value = "/private/users/import", method = RequestMethod.POST)
	public String importUsers(@RequestParam MultipartFile usersCSV, Model model, RedirectAttributes redirectAttributes) throws IOException,
			ParseException {

		try {
			systemUserService.importUsers(usersCSV);
			successMessage(redirectAttributes, "userAdmin.import.success");
		} catch (ValidationException e) {
			errorMessage(redirectAttributes, e);
		}
		return "redirect:/private/users";
	}

	@RequestMapping(value = "/private/users/report/{format}", method = RequestMethod.GET)
	public void exportUsers(@PathVariable String format, HttpServletResponse response) {
		try {
			List<SystemUser> users = systemUserRepository.findAll();
			systemUserReport.generateUserReport(users, response.getOutputStream());
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=\"document.pdf\"");
			response.flushBuffer();
		} catch (IOException ex) {
			throw new ReportException(ex);
		}
	}

	/**
	 * Mapping between role and the label. It is used by Spring to render
	 * multi-select
	 * 
	 * @return mapping between enum and the labels
	 */
	private Map<String, String> getAllRoles() {
		Map<String, String> map = new HashMap<String, String>();
		for (UserRoleEnum userRole : UserRoleEnum.values()) {
			map.put(userRole.toString(), MessageUtils.translateEnum(userRole));
		}
		return map;
	}

	private boolean hasErrorPasswordChange(BindingResult result, Model model, SystemUser user) {
		if (result.hasErrors()) {
			return true;
		} else if (!user.getPassword().equals(user.getPasswordCheck())) {
			result.rejectValue("passwordCheck", "userAdmin.notMatchingPasswords");
			return true;
		} else if (!StringUtils.isEmpty(user.getOldPlainPassword()) && !BCrypt.checkpw(user.getOldPlainPassword(), user.getOldPassword())) {
			result.addError(new FieldError("systemUser", "oldPlainPassword", getMessage("userAdmin.profile.oldPassword.error")));
			return true;
		}
		return false;
	}
}
