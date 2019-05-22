/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * This class is only a styling demo for our code styling conventions and for testing IDE setting of each developer.
 * <p>
 * It is only used to test whether all developers have same IDE settings for developing.
 * <p>
 * Please do never commit changes to this class.
 * <p>
 * Especially take care to never clean up formatting inside of this class in case you have to commit changes to it.
 */
public class DummyIdeFormattingSettingTestClass {

	public void test_method_that_can_be_used_to_check_formatting_on_auto_save_inside() {
		// put your code to format here

		// then check that no other line of this class gets formatted unexpectedly
	}

	/**
	 * comments should never be joined
	 * when having line breaks in between
	 * like in this comment
	 */
	public void below_this_method_is_a_indented_empty_line() {
		/*
		 *
		 * comments should never be joined
		 * when having line breaks in between
		 * like in this comment
		 */
	}

	public void below_this_method_is_no_indented_empty_line() {
		// just an empty code block
	}

	public void below_this_method_is_another_indented_empty_line() {
		// just a dummy empty code block with automatic line wrapping due to formatting correctly bla bla bla bla bla
		// bla bla bla bla bla bla bla bla bla bla
	}

}
