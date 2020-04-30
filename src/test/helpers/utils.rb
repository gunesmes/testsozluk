
def valid_json?(file_content)
	JSON.parse(file_content)
	return true
rescue JSON::ParserError => e
	return false
end

def has_duplicated_terms?(file_content)
	term_list = []
	json_terms = JSON.parse(file_content)

	json_terms.each do |term|
	  term_name = term["term"].downcase
		
		if term_list.include? term_name
			return "Failed: `#{term_name.upcase}` has already been added. Instead of adding same erm, please do correction on it."
		else
			term_list << term_name
		end
	end

	return true
end

def has_key_pairs?(file_content)
	json_terms = JSON.parse(file_content)

	json_terms.each do |term|
		if term["term"].nil? or term["meaning"].nil?
			return "Failed: #{term} should have both of `term` and `meaning` description"
		end
	end

	return true
end